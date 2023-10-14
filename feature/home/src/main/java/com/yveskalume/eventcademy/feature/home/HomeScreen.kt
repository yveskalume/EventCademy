package com.yveskalume.eventcademy.feature.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.yveskalume.eventcademy.core.designsystem.components.AdvertisementsItem
import com.yveskalume.eventcademy.core.designsystem.components.EmptyAnimation
import com.yveskalume.eventcademy.core.designsystem.components.EventItem
import com.yveskalume.eventcademy.core.designsystem.components.EventLargeItem
import com.yveskalume.eventcademy.core.designsystem.components.LoadingAnimation
import com.yveskalume.eventcademy.core.designsystem.components.SectionHeader
import com.yveskalume.eventcademy.core.designsystem.theme.ThemePreview
import com.yveskalume.eventcademy.core.domain.model.Advertisement
import com.yveskalume.eventcademy.core.domain.model.AdvertisementType
import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.designsystem.R as DesignSystemR

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onEventClick: (String) -> Unit,
    onSettingClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler {
        (context as? Activity)?.finish()
    }
    HomeScreen(
        uiState = uiState,
        onEventClick = onEventClick,
        onSettingClick = onSettingClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onEventClick: (String) -> Unit,
    onSettingClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(end = 4.dp),
                title = {
                    SubcomposeAsyncImage(
                        model = DesignSystemR.drawable.eventcademy_text_logo,
                        contentDescription = null,
                        modifier = Modifier
                            .width(150.dp),
                        contentScale = ContentScale.Inside
                    )
                },
                actions = {
                    IconButton(onClick = onSettingClick) {
                        Icon(imageVector = Icons.Outlined.Settings, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                HomeUiState.Empty -> {
                    EmptyAnimation(text = "Aucun événement pour le moment")
                }

                is HomeUiState.Error -> {
                    EmptyAnimation(text = "Une erreur est survenue")
                }

                HomeUiState.Loading -> {
                    LoadingAnimation()
                }

                is HomeUiState.Success -> {
                    HomeScreenContent(
                        modifier = Modifier.fillMaxSize(),
                        ads = uiState.advertisements,
                        upcomingEvents = uiState.upcomingEvents,
                        pastEvents = uiState.pastEvents,
                        onEventClick = onEventClick
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    ads: List<Advertisement>,
    upcomingEvents: List<Event>,
    pastEvents: List<Event>,
    onEventClick: (eventUid: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (ads.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                SectionHeader(title = "Annonces")
            }
            item(span = { GridItemSpan(2) }, key = "ads") {
                AdvertisementsItem(
                    items = ads,
                    onItemClick = { advertisement ->
                        if (advertisement.type == AdvertisementType.INTERNAL_EVENT) {
                            onEventClick(advertisement.destination)
                        } else if (advertisement.type == AdvertisementType.EXTERNAL_CONTENT) {
                            uriHandler.openUri(advertisement.destination)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (upcomingEvents.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                SectionHeader(title = "Événements à venir")
            }
            items(
                span = { GridItemSpan(2) },
                items = upcomingEvents,
                key = { it.uid }
            ) { event ->
                EventLargeItem(
                    event = event,
                    onClick = { onEventClick(event.uid) }
                )
            }
        }
        if (pastEvents.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                SectionHeader(title = "Événements passés")
            }
            items(items = pastEvents, key = { it.uid }) { event ->
                EventItem(
                    event = event,
                    onClick = { onEventClick(event.uid) })
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    ThemePreview {
        HomeScreen(
            uiState = HomeUiState.Loading,
            onEventClick = {},
            onSettingClick = {})
    }
}