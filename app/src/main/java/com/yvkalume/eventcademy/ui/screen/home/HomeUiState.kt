package com.yvkalume.eventcademy.ui.screen.home

import com.yvkalume.eventcademy.data.entity.Advertisement
import com.yvkalume.eventcademy.data.entity.Event

sealed interface HomeUiState {
    object Loading : HomeUiState
    object Empty : HomeUiState
    data class Success(
        val events: List<Event>,
        val advertisements: List<Advertisement>
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}