package com.yveskalume.eventcademy.ui.screen.eventdetail

import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.model.EventBooking
import com.yveskalume.eventcademy.core.domain.model.User

sealed interface EventDetailUiState {
    object Loading : EventDetailUiState
    data class Success(val event: Event, val organizer: User?, val bookings: List<EventBooking>) : EventDetailUiState
    data class Error(val errorMessage: String) : EventDetailUiState
}