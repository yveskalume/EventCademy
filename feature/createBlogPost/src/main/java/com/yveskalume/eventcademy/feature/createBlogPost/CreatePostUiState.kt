package com.yveskalume.eventcademy.feature.createBlogPost


sealed interface CreatePostUiState {
    object Initial : CreatePostUiState
    object Loading : CreatePostUiState
    object Success : CreatePostUiState
    data class Error(val message: String) : CreatePostUiState
}