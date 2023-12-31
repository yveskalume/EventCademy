package com.yveskalume.eventcademy.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.google.firebase.auth.FirebaseAuth
import com.yveskalume.eventcademy.feature.auth.AuthRoute
import com.yveskalume.eventcademy.feature.bookmark.BookmarkRoute
import com.yveskalume.eventcademy.feature.createevent.CreateEventRoute
import com.yveskalume.eventcademy.feature.eventdetail.EventDetailRoute
import com.yveskalume.eventcademy.feature.home.HomeRoute
import com.yveskalume.eventcademy.feature.profile.ProfileRoute
import com.yveskalume.eventcademy.feature.setting.SettingRoute

@Composable
fun AppNavHost(
    modifier: Modifier,
    navController: NavHostController,
    webClientIdToken: String,
    isDarkTheme: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    val auth = remember { FirebaseAuth.getInstance() }

    fun getStartDestination(): String {
        return if (auth.currentUser == null) {
            Destination.AuthScreen.route
        } else {
            Destination.HomeScreen.route
        }
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = getStartDestination()
    ) {
        composable(route = Destination.AuthScreen.route) {
            AuthRoute(
                webClientIdToken = webClientIdToken,
                onConnectSuccess = {
                    navController.navigate(Destination.HomeScreen)
                })
        }

        composable(route = Destination.HomeScreen.route) {
            HomeRoute(
                onEventClick = { eventUid ->
                    navController.navigate(
                        Destination.EventDetailScreen.createRoute(
                            eventUid = eventUid
                        )
                    )
                },
                onSettingClick = { navController.navigate(Destination.SettingsScreen) }
            )
        }

        composable(
            route = Destination.EventDetailScreen.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://eventcademy.app/event/{eventUid}z"
            })

        ) {
            EventDetailRoute(
                onBackClick = navController::navigateUp
            )
        }

        composable(route = Destination.BookmarkScreen.route) {
            BookmarkRoute(
                onEventClick = { eventUid ->
                    navController.navigate(
                        Destination.EventDetailScreen.createRoute(
                            eventUid = eventUid
                        )
                    )
                }
            )
        }
        composable(route = Destination.SettingsScreen.route) {
            SettingRoute(
                onBackClick = { navController.navigateUp() },
                darkMode = isDarkTheme,
                onDarkModeChange = onDarkModeChange
            )
        }

        composable(route = Destination.ProfileScreen.route) {
            ProfileRoute(
                onAddEventClick = {
                    navController.navigate(Destination.CreateEventScreen)
                },
                onEventClick = { evenUid ->
                    navController.navigate(
                        Destination.EventDetailScreen.createRoute(
                            eventUid = evenUid
                        )
                    )
                },
                onLogoutClick = {
                    auth.signOut()
                    navController.navigate(Destination.AuthScreen)
                }
            )
        }

        composable(route = Destination.CreateEventScreen.route) {
            CreateEventRoute(
                onBackClick = { navController.navigateUp() },
            )
        }
    }
}