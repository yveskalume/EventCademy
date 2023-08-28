package com.yveskalume.eventcademy.ui.screen.profile

import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.model.User

sealed interface ProfileUiState {
    object Loading : ProfileUiState
    data class Success(val user: User, val userEvents: List<Event>) : ProfileUiState
    data class Error(val errorMessage: String) : ProfileUiState
}