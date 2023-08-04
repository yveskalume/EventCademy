package com.yvkalume.eventcademy.ui.screen.eventdetail

import com.yvkalume.eventcademy.data.entity.Event
import com.yvkalume.eventcademy.data.entity.EventBooking

sealed interface EventDetailUiState {
    object Loading : EventDetailUiState
    data class Success(val event: Event, val bookings: List<EventBooking>) : EventDetailUiState
    data class Error(val errorMessage: String) : EventDetailUiState
}