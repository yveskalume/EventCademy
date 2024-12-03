package com.yveskalume.eventcademy.core.ui

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavOptionsBuilder

sealed class Destination(val route: String) {
    object AuthScreen : Destination("auth")
    object HomeScreen : Destination("home")
    object EventDetailScreen : Destination("event-detail/{eventUid}") {
        fun createRoute(eventUid: String): String {
            return "event-detail/$eventUid"
        }
    }

    object BookmarkScreen : Destination("bookmark")
    object SettingsScreen : Destination("settings")
    object CreateEventScreen : Destination("create-event")
    object ProfileScreen : Destination("profile")

    //blog
    object BlogHomeScreen: Destination("blog-home")
    object CreatePostScreen: Destination("create-post")
    object PostDetailsScreen: Destination("post-details/{postUid}"){
        fun createRoute(postUid: String): String{
            return "post-details/$postUid"
        }
    }
}

fun NavDestination?.isCurrent(destination: Destination): Boolean {
    return this?.hierarchy?.any { it.route == destination.route } == true
}

fun NavController.navigate(
    destination: Destination,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) {

    // some times this throw an illegalStateException (should figure out why)
    if (builder != null) {
        try {
            navigate(destination.route, builder)
        } catch (e: Exception) {
            navigate(destination.route)
        }
    } else {
        navigate(destination.route)
    }
}