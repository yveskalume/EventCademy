package com.yveskalume.eventcademy.feature.bookmark

import com.yveskalume.eventcademy.core.domain.model.EventBooking

sealed interface BookmarkUiState {
    object Loading : BookmarkUiState
    data class Success(val eventBookings: List<EventBooking>) : BookmarkUiState
    data class Error(val message: String) : BookmarkUiState
}