package com.yveskalume.eventcademy.feature.eventdetail

sealed interface EventDetailUiEffect {
    data object ShowCongratulations : EventDetailUiEffect
}