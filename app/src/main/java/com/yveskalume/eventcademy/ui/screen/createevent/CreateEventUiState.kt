package com.yveskalume.eventcademy.ui.screen.createevent

sealed interface CreateEventUiState {
    object Initial : CreateEventUiState
    object Loading : CreateEventUiState
    object Success : CreateEventUiState
    data class Error(val message: String) : CreateEventUiState
}