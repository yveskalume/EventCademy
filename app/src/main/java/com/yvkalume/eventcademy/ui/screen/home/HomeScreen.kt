package com.yvkalume.eventcademy.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yvkalume.eventcademy.R
import com.yvkalume.eventcademy.ui.components.EventItem
import com.yvkalume.eventcademy.ui.components.FeaturedEventItem
import com.yvkalume.eventcademy.util.ThemePreview

@Composable
fun HomeRoute(onEventClick: () -> Unit) {
    HomeScreen(onEventClick = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(onEventClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                },
                title = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(text = "Bonjour")
                        Text(text = "Yves Kalume", style = MaterialTheme.typography.titleMedium)
                    }
                },
                actions = {
                    Icon(imageVector = Icons.Outlined.Settings, contentDescription = null)
                }
            )
        }
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(it),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                FeaturedEventItem(onClick = onEventClick, modifier = Modifier.aspectRatio(1.2f))
            }
            items(10) {
                EventItem(onEventClick = onEventClick)
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    ThemePreview {
        HomeScreen(onEventClick = {})
    }
}