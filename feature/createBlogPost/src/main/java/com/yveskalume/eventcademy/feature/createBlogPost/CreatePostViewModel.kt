package com.yveskalume.eventcademy.feature.createBlogPost

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yveskalume.eventcademy.core.domain.model.Post
import com.yveskalume.eventcademy.core.domain.repository.EventRepository
import com.yveskalume.eventcademy.core.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postRepository: PostRepository,
): ViewModel() {
    private val _uiState: MutableStateFlow<CreatePostUiState> =
        MutableStateFlow(CreatePostUiState.Initial)

    val uiState: StateFlow<CreatePostUiState> = _uiState.asStateFlow()

    fun createPost(post: Post){
        viewModelScope.launch {
            _uiState.emit(CreatePostUiState.Loading)
            try {
                postRepository.createPost(post)
                _uiState.emit(CreatePostUiState.Success)
            } catch (e: Exception) {
                _uiState.emit(CreatePostUiState.Error("Une erreur est survenue"))
            }
        }
    }
}