package com.yveskalume.eventcademy.core.ui

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yveskalume.eventcademy.core.data.preferences.SettingDataStorePreferences
import com.yveskalume.eventcademy.core.designsystem.theme.EventCademyTheme
import kotlinx.coroutines.launch

fun ComponentActivity.installUi(webClientIdToken: String) {
    val dataStoreUtil = SettingDataStorePreferences(applicationContext)

    val systemTheme =
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                true
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                false
            }

            else -> {
                false
            }
        }

    setContent {

        val isDarkTheme by dataStoreUtil.getTheme(systemTheme)
            .collectAsStateWithLifecycle(initialValue = systemTheme)

        val coroutineScope = rememberCoroutineScope()


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
                        AnimatedVisibility(
                            visible = shouldShowBottomNavigation(destination),
                            enter = slideInVertically { it / 2 },
                            exit = slideOutVertically { it / 2},
                        ) {
                            BottomNavigationBar(
                                navController = navController,
                                destination = destination
                            )
                        }
                    }
                ) {
                    AppNavHost(
                        modifier = Modifier.padding(it),
                        navController = navController,
                        webClientIdToken = webClientIdToken,
                        isDarkTheme = isDarkTheme,
                        onDarkModeChange = { darkMode ->
                            coroutineScope.launch {
                                dataStoreUtil.saveTheme(
                                    darkMode
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

private fun shouldShowBottomNavigation(destination: NavDestination?): Boolean {
    return !destination.isCurrent(Destination.AuthScreen)
            && !destination.isCurrent(Destination.EventDetailScreen)
            && !destination.isCurrent(Destination.SettingsScreen)
}