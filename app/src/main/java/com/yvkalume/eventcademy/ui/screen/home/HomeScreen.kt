package com.yvkalume.eventcademy.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yvkalume.eventcademy.ui.components.EventVerticalItem
import com.yvkalume.eventcademy.util.ThemePreview
import kiwi.orbit.compose.ui.controls.Scaffold
import kiwi.orbit.compose.ui.controls.Text
import kiwi.orbit.compose.ui.controls.TopAppBar

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "EventCademy") })
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(4) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(8) {
                        EventVerticalItem()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    ThemePreview {
        HomeScreen(navController = rememberNavController())
    }
}