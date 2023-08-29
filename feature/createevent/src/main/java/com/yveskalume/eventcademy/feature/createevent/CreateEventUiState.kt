package com.yveskalume.eventcademy.feature.createevent

sealed interface CreateEventUiState {
    object Initial : CreateEventUiState
    object Loading : CreateEventUiState
    object Success : CreateEventUiState
    data class Error(val message: String) : CreateEventUiState
}