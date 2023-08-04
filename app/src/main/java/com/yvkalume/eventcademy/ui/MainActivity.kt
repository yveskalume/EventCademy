package com.yvkalume.eventcademy.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.yvkalume.eventcademy.ui.navigation.Destination
import com.yvkalume.eventcademy.ui.navigation.isCurrent
import com.yvkalume.eventcademy.ui.screen.auth.AuthRoute
import com.yvkalume.eventcademy.ui.screen.bookmark.BookmarkRoute
import com.yvkalume.eventcademy.ui.screen.eventdetail.EventDetailRoute
import com.yvkalume.eventcademy.ui.screen.home.HomeRoute
import com.yvkalume.eventcademy.ui.screen.setting.SettingRoute
import com.yvkalume.eventcademy.ui.theme.EventCademyTheme
import com.yvkalume.eventcademy.util.navigate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var auth: FirebaseAuth


    private fun getStartDestination(): String {
        Log.e("MainActivity", "getStartDestination: ${auth.currentUser}")
        return if (auth.currentUser == null) {
            Destination.AuthScreen.route
        } else {
            Destination.HomeScreen.route
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {

            var isDarkTheme by remember { mutableStateOf(false) }

            EventCademyTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val destination = navController
                        .currentBackStackEntryAsState().value?.destination

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            if (shouldShowBottomNavigation(destination)) {
                                NavigationBar {
                                    NavigationBarItem(
                                        selected = destination.isCurrent(Destination.HomeScreen),
                                        onClick = { navController.navigate(Destination.HomeScreen) },
                                        label = {
                                            Text(text = "Accueil")
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Rounded.Home,
                                                contentDescription = null
                                            )
                                        }
                                    )

                                    NavigationBarItem(
                                        selected = destination.isCurrent(Destination.BookmarkScreen),
                                        onClick = { navController.navigate(Destination.BookmarkScreen) },
                                        label = {
                                            Text(text = "Vos événements")
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Rounded.Bookmark,
                                                contentDescription = null
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    ) {
                        NavHost(
                            modifier = Modifier.padding(it),
                            navController = navController,
                            startDestination = getStartDestination()
                        ) {
                            composable(route = Destination.AuthScreen.route) {
                                AuthRoute(onConnectSuccess = { navController.navigate(Destination.HomeScreen) })
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

                            composable(route = Destination.EventDetailScreen.route) { backStackEntry ->
                                val eventUid = backStackEntry.arguments?.getString("eventUid")
                                EventDetailRoute(
                                    eventUid = eventUid,
                                    onBackClick = navController::navigateUp
                                )
                            }

                            composable(route = Destination.BookmarkScreen.route) {
                                BookmarkRoute(onEventClick = { navController.navigate(Destination.EventDetailScreen) })
                            }
                            composable(route = Destination.SettingsScreen.route) {
                                SettingRoute(
                                    onBackClick = { navController.navigateUp() },
                                    onLogoutClick = {
                                        auth.signOut()
                                        navController.navigate(Destination.AuthScreen)
                                    },
                                    darkMode = isDarkTheme,
                                    onDarkModeChange = { darkMode -> isDarkTheme = darkMode }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun shouldShowBottomNavigation(destination: NavDestination?): Boolean {
    return !destination.isCurrent(Destination.AuthScreen) && !destination.isCurrent(Destination.EventDetailScreen)
}