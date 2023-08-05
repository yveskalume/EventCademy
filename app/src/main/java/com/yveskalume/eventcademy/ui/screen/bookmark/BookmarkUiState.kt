package com.yveskalume.eventcademy.ui.screen.bookmark

import com.yveskalume.eventcademy.data.entity.EventBooking

sealed interface BookmarkUiState {
    object Loading : BookmarkUiState
    data class Success(val eventBookings: List<EventBooking>) : BookmarkUiState
    data class Error(val message: String) : BookmarkUiState
}