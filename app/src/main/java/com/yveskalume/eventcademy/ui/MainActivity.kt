package com.yveskalume.eventcademy.ui

import android.Manifest
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.auth.FirebaseAuth
import com.yveskalume.eventcademy.ui.navigation.Destination
import com.yveskalume.eventcademy.ui.navigation.isCurrent
import com.yveskalume.eventcademy.ui.screen.auth.AuthRoute
import com.yveskalume.eventcademy.ui.screen.bookmark.BookmarkRoute
import com.yveskalume.eventcademy.ui.screen.eventdetail.EventDetailRoute
import com.yveskalume.eventcademy.ui.screen.home.HomeRoute
import com.yveskalume.eventcademy.ui.screen.setting.SettingRoute
import com.yveskalume.eventcademy.ui.theme.EventCademyTheme
import com.yveskalume.eventcademy.util.DataStoreUtil
import com.yveskalume.eventcademy.util.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var auth: FirebaseAuth
    private fun getStartDestination(): String {
        return if (auth.currentUser == null) {
            Destination.AuthScreen.route
        } else {
            Destination.HomeScreen.route
        }
    }

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        checkForUpdates()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            askNotificationPermission()
        }

        val dataStoreUtil = DataStoreUtil(applicationContext)

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
                            if (shouldShowBottomNavigation(destination)) {
                                NavigationBar {
                                    NavigationBarItem(
                                        selected = destination.isCurrent(Destination.HomeScreen),
                                        onClick = {
                                            if (!destination.isCurrent(Destination.HomeScreen)) {
                                                navController.navigate(Destination.HomeScreen)
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
                                                navController.navigate(Destination.BookmarkScreen)
                                            }
                                        },
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

                            composable(
                                route = Destination.EventDetailScreen.route,
                                deepLinks = listOf(navDeepLink {
                                    uriPattern = "https://eventcademy.app/event/{eventUid}"
                                })

                            ) { backStackEntry ->
                                val eventUid = backStackEntry.arguments?.getString("eventUid")
                                EventDetailRoute(
                                    eventUid = eventUid,
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
                                    onLogoutClick = {
                                        auth.signOut()
                                        navController.navigate(Destination.AuthScreen)
                                    },
                                    darkMode = isDarkTheme,
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
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun askNotificationPermission() {
        val launcher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted.not()) {
                Toast.makeText(
                    this,
                    "Veuillez autoriser les notifications pour recevoir les notifications de l'application",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    appUpdateManager.startUpdateFlow(
                        appUpdateInfo,
                        this,
                        AppUpdateOptions.newBuilder(IMMEDIATE).build()
                    )
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // get destination from intent (destination may come from FCM)
        val destination = intent?.getStringExtra("destination")
        Log.d("MainActivity", "onNewIntent: $destination")
        if (destination != null) {
            try {
                Intent(
                    Intent.ACTION_VIEW,
                    destination.toUri(),
                    this,
                    MainActivity::class.java
                ).also {
                    startActivity(it)
                    finish()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "onNewIntent: ", e)
            }
        }
    }

    private fun checkForUpdates() {


        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)
            ) {

                appUpdateManager.startUpdateFlow(
                    appUpdateInfo,
                    this,
                    AppUpdateOptions.newBuilder(IMMEDIATE).build()
                )

            }
        }
    }
}

private fun shouldShowBottomNavigation(destination: NavDestination?): Boolean {
    return !destination.isCurrent(Destination.AuthScreen) && !destination.isCurrent(Destination.EventDetailScreen)
}