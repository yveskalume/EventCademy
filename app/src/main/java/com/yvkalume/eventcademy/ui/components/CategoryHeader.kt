package com.yvkalume.eventcademy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import kiwi.orbit.compose.ui.controls.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kiwi.orbit.compose.ui.OrbitTheme

@Composable
fun CategoryHeader(modifier: Modifier = Modifier) {
    Row(modifier = Modifier.fillMaxWidth().then(modifier), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "Category", style = OrbitTheme.typography.title3)
        Text(text = "Voir Tout", style = OrbitTheme.typography.title4.copy(color = Color.Blue))
    }
}