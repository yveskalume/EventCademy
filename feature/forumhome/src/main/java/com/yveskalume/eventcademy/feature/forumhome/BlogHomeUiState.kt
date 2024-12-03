package com.yveskalume.eventcademy.feature.forumhome

import com.yveskalume.eventcademy.core.domain.model.Advertisement
import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.model.Post


sealed interface BlogHomeUiState {
    object Loading : BlogHomeUiState
    object Empty : BlogHomeUiState
    data class Success(
        val posts: List<Post>,
    ) : BlogHomeUiState
    data class Error(val throwable: Throwable) : BlogHomeUiState
}