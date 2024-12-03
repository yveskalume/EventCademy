package com.yveskalume.eventcademy.feature.forumhome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yveskalume.eventcademy.core.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class BlogHomeViewModel @Inject constructor(
    postRepository: PostRepository
): ViewModel() {

    val uiState: StateFlow<BlogHomeUiState> = postRepository.getAllPosts()
        .map { posts ->
            BlogHomeUiState.Success(posts)
        }
        .catch <BlogHomeUiState>{
            emit(BlogHomeUiState.Error(it))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BlogHomeUiState.Loading
        )

}