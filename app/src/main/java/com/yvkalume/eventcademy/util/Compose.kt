package com.yvkalume.eventcademy.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.yvkalume.eventcademy.ui.navigation.Destination
import com.yvkalume.eventcademy.ui.theme.EventCademyTheme
import kiwi.orbit.compose.ui.controls.Surface

@Composable
fun ThemePreview(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    EventCademyTheme {
        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.background,
            content = content
        )
    }
}

fun NavController.navigate(destination: Destination) {
    navigate(destination.route)
}