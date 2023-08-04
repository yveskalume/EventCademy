package com.yvkalume.eventcademy.ui.screen.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yvkalume.eventcademy.data.entity.EventBooking
import com.yvkalume.eventcademy.data.util.isFuture
import com.yvkalume.eventcademy.data.util.isPast
import com.yvkalume.eventcademy.ui.components.EventBookmarkItem
import com.yvkalume.eventcademy.util.ThemePreview

@Composable
fun BookmarkRoute(
    viewModel: BookmarkViewModel = hiltViewModel(),
    onEventClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BookmarkScreen(uiState = uiState, onEventClick = onEventClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookmarkScreen(uiState: BookmarkUiState, onEventClick: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Vos événements")
                }
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            when (uiState) {
                is BookmarkUiState.Error -> {}
                BookmarkUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is BookmarkUiState.Success -> {
                    BookmarkContent(
                        eventBookings = uiState.eventBookings,
                        onEventClick = onEventClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookmarkContent(
    eventBookings: List<EventBooking>,
    onEventClick: (eventUid: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var showPastEvents by remember {
        mutableStateOf(false)
    }

    val events = remember(eventBookings, showPastEvents) {
        derivedStateOf {
            if (showPastEvents) {
                eventBookings.filter { it.eventDate.isPast }
            } else {
                eventBookings.filter { it.eventDate.isFuture }
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = !showPastEvents,
                    onClick = { showPastEvents = false },
                    label = { Text(text = "À venir",modifier = Modifier.padding(16.dp)) })

                FilterChip(
                    selected = showPastEvents,
                    onClick = { showPastEvents = true },
                    label = { Text(text = "Passés",modifier = Modifier.padding(16.dp)) }
                )
            }
        }

        items(items = events.value) { booking ->
            EventBookmarkItem(
                eventBooking = booking,
                onClick = { onEventClick(booking.eventUid) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun BookmarkScreenPreview() {
    ThemePreview {
        BookmarkScreen(uiState = BookmarkUiState.Loading, onEventClick = {})
    }
}