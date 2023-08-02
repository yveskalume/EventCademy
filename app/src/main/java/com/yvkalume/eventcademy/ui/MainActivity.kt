package com.yvkalume.eventcademy.ui

import android.annotation.SuppressLint
import android.os.Bundle
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
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yvkalume.eventcademy.ui.navigation.Destination
import com.yvkalume.eventcademy.ui.navigation.isCurrent
import com.yvkalume.eventcademy.ui.screen.auth.AuthRoute
import com.yvkalume.eventcademy.ui.screen.bookmark.BookmarkRoute
import com.yvkalume.eventcademy.ui.screen.eventdetail.EventDetailRoute
import com.yvkalume.eventcademy.ui.screen.home.HomeRoute
import com.yvkalume.eventcademy.ui.theme.EventCademyTheme
import com.yvkalume.eventcademy.util.navigate

class MainActivity : ComponentActivity() {
    @SuppressLint("MaterialDesignInsteadOrbitDesign")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            EventCademyTheme {
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
                            startDestination = Destination.AuthScreen.route
                        ) {
                            composable(route = Destination.AuthScreen.route) {
                                AuthRoute(onConnectSuccess = { navController.navigate(Destination.HomeScreen) })
                            }

                            composable(route = Destination.HomeScreen.route) {
                                HomeRoute(
                                    onEventClick = { navController.navigate(Destination.EventDetailScreen) }
                                )
                            }

                            composable(route = Destination.EventDetailScreen.route) {
                                EventDetailRoute(onBackClick = navController::navigateUp)
                            }

                            composable(route = Destination.BookmarkScreen.route) {
                                BookmarkRoute(onEventClick = { navController.navigate(Destination.EventDetailScreen) })
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