package com.yveskalume.eventcademy.ui.screen.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yveskalume.eventcademy.core.domain.model.EventBooking
import com.yveskalume.eventcademy.util.isFuture
import com.yveskalume.eventcademy.util.isPast
import com.yveskalume.eventcademy.ui.components.EmptyAnimation
import com.yveskalume.eventcademy.ui.components.EventBookmarkItem
import com.yveskalume.eventcademy.ui.components.LoadingAnimation
import com.yveskalume.eventcademy.util.ThemePreview

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
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is BookmarkUiState.Error -> {
                    EmptyAnimation(text = "Une erreur est survenue")
                }

                BookmarkUiState.Loading -> {
                    LoadingAnimation()
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

    if (events.value.isEmpty()) {
        EmptyAnimation(
            text = "Vous n'avez pas encore d'événements",
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
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