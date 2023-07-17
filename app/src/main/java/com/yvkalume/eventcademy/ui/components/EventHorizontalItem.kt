package com.yvkalume.eventcademy.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yvkalume.eventcademy.R
import com.yvkalume.eventcademy.util.ThemePreview
import kiwi.orbit.compose.ui.OrbitTheme
import kiwi.orbit.compose.ui.controls.SurfaceCard
import kiwi.orbit.compose.ui.controls.Text

@Composable
fun EventHorizontalItem() {
    SurfaceCard(onClick = {}) {
        Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column {
                Text(text = "Mardi 21 Juillet - 12h00", style = OrbitTheme.typography.bodyLarge)
                Text(text = "Lorem ipsum", style = OrbitTheme.typography.title2)
            }
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun EventHorizontalItemPreview() {
    ThemePreview {
        EventHorizontalItem()
    }
}