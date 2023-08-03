package com.yvkalume.eventcademy.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.yvkalume.eventcademy.ui.navigation.Destination.BookmarkScreen
import com.yvkalume.eventcademy.ui.navigation.Destination.HomeScreen

sealed class Destination(val route: String) {
    object AuthScreen : Destination("auth")
    object HomeScreen : Destination("home")
    object EventDetailScreen : Destination("event-detail")
    object BookmarkScreen : Destination("bookmark")
    object SettingsScreen : Destination("settings")
}
fun NavDestination?.isCurrent(destination: Destination): Boolean {
    return this?.hierarchy?.any { it.route == destination.route } == true
}