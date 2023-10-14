package com.yveskalume.eventcademy.feature.home

import com.yveskalume.eventcademy.core.domain.model.Advertisement
import com.yveskalume.eventcademy.core.domain.model.Event

sealed interface HomeUiState {
    object Loading : HomeUiState
    object Empty : HomeUiState
    data class Success(
        val upcomingEvents: List<Event>,
        val pastEvents: List<Event>,
        val advertisements: List<Advertisement>
    ) : HomeUiState
    data class Error(val throwable: Throwable) : HomeUiState
}