package com.yvkalume.eventcademy.ui.navigation

sealed class Destination(val route: String) {
    object HomeScreen : Destination("home")
    object EventDetailScreen : Destination("event-detail")
}