package com.yveskalume.eventcademy.feature.postdetail

import com.yveskalume.eventcademy.core.domain.model.Post
import com.yveskalume.eventcademy.core.domain.model.PostLike
import com.yveskalume.eventcademy.core.domain.model.User


sealed interface PostDetailsUiState {
    data object Loading : PostDetailsUiState
    data class Success(val post: Post, val publisher: User?, val likes: List<PostLike>) :
        PostDetailsUiState
    data class Error(val errorMessage: String) : PostDetailsUiState
}