package com.yvkalume.eventcademy.ui.screen.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yvkalume.eventcademy.ui.components.EventBookmarkItem
import com.yvkalume.eventcademy.util.ThemePreview

@Composable
fun BookmarkRoute(onEventClick: () -> Unit) {
    BookmarkScreen(onEventClick = onEventClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookmarkScreen(onEventClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Vos événements",)
                })
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { /*TODO*/ }, shape = RoundedCornerShape(8.dp)) {
                        Text(text = "À venir")
                    }
                    Button(onClick = { /*TODO*/ }, shape = RoundedCornerShape(8.dp)) {
                        Text(text = "Passés")
                    }
                }
            }

            items(10) {
                EventBookmarkItem(onClick = onEventClick,modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Preview
@Composable
fun BookmarkScreenPreview() {
    ThemePreview {
        BookmarkScreen(onEventClick = {})
    }
}