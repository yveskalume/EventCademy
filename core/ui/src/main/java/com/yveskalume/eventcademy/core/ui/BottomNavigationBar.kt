package com.yveskalume.eventcademy.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(navController: NavHostController, destination: NavDestination?) {
    NavigationBar {
        NavigationBarItem(
            selected = destination.isCurrent(Destination.HomeScreen),
            onClick = {
                if (!destination.isCurrent(Destination.HomeScreen)) {
                    navController.navigate(Destination.HomeScreen) {
                        popUpTo(Destination.HomeScreen.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
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
            onClick = {
                if (!destination.isCurrent(Destination.BookmarkScreen)) {
                    navController.navigate(Destination.BookmarkScreen) {
                        popUpTo(Destination.HomeScreen.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            label = {
                Text(text = "Agenda")
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Bookmark,
                    contentDescription = null
                )
            }
        )

        NavigationBarItem(
            selected = destination.isCurrent(Destination.ProfileScreen),
            onClick = {
                if (!destination.isCurrent(Destination.ProfileScreen)) {
                    navController.navigate(Destination.ProfileScreen) {
                        popUpTo(Destination.HomeScreen.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            label = {
                Text(text = "Profil")
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null
                )
            }
        )
    }
}