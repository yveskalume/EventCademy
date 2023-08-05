package com.yveskalume.eventcademy.ui.screen.home

import com.yveskalume.eventcademy.data.entity.Advertisement
import com.yveskalume.eventcademy.data.entity.Event

sealed interface HomeUiState {
    object Loading : HomeUiState
    object Empty : HomeUiState
    data class Success(
        val events: List<Event>,
        val advertisements: List<Advertisement>
    ) : HomeUiState
    data class Error(val throwable: Throwable) : HomeUiState
}