package com.yveskalume.eventcademy.feature.postdetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yveskalume.eventcademy.core.domain.model.Post
import com.yveskalume.eventcademy.core.domain.repository.PostLikesRepository
import com.yveskalume.eventcademy.core.domain.repository.PostRepository
import com.yveskalume.eventcademy.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val postLikesRepository: PostLikesRepository
): ViewModel() {
    private val postUid = savedStateHandle.get<String>("postUid")
    
    private val _uiState: MutableStateFlow<PostDetailsUiState> = MutableStateFlow(PostDetailsUiState.Loading)
    val uiState: StateFlow<PostDetailsUiState> = _uiState.asStateFlow()
    
    private val _postLikesState: MutableStateFlow<PostLikesState> = MutableStateFlow(PostLikesState.LOADING)
    val postLikesState: StateFlow<PostLikesState> = _postLikesState.asStateFlow()

    init {
        getData()
    }

    private fun getData(){
        if (postUid != null){
            getPost(postUid)
            checkIfUserHasLiked(postUid)
        }
    }

    private fun checkIfUserHasLiked(postUid: String){
        viewModelScope.launch{
            try {
                postLikesRepository.checkIfUserHasLiked(postUid).collect{hasLiked->
                    if (hasLiked){
                        _postLikesState.emit(PostLikesState.LIKED)
                    }else{
                        _postLikesState.emit(PostLikesState.NOT_LIKED)
                    }
                }
            }catch (t: Throwable){
                Log.e("PostDetailViewModel", "checkIfUserHasLiked: ${t.message}")
            }
        }
    }
    
    private fun getPost(postUid: String){
        viewModelScope.launch{
            _uiState.emit(PostDetailsUiState.Loading)
            try {
                val postDeferred = async{
                    postRepository.getPostByUid(postUid)
                }
                val post = postDeferred.await()
                if (post == null){
                    _uiState.emit(PostDetailsUiState.Error("Ce post n'est pas encore disponible ou a été supprimé"))
                    return@launch
                }
                
                val userDeferred = async{
                    userRepository.getUserByUid(post.userUid)
                }
                postLikesRepository.getAllLikesByPostUid(postUid).collect{likes->
                    _uiState.emit(PostDetailsUiState.Success(post, userDeferred.await(), likes))
                }
            }catch (t: Throwable){
                _uiState.emit(PostDetailsUiState.Error(t.message ?: "Une erreur est survenue"))
            }
        }
    }
    
    fun toggleUserPostLIke(post: Post, postLikesState: PostLikesState){
        viewModelScope.launch{
            try {
                if (postLikesState == PostLikesState.LIKED){
                    postLikesRepository.deleteLike(post.uid)
                } else{
                    postLikesRepository.createLike(post)
                }
            }catch (t: Throwable){
                _uiState.emit(PostDetailsUiState.Error(t.message ?: "Une erreur est survenue"))
            }
        }
    }
}