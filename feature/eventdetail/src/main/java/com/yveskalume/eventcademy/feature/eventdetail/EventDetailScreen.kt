package com.yveskalume.eventcademy.feature.eventdetail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.SubcomposeAsyncImage
import com.yveskalume.eventcademy.core.designsystem.R
import com.yveskalume.eventcademy.core.designsystem.components.EmptyAnimation
import com.yveskalume.eventcademy.core.designsystem.components.LoadingAnimation
import com.yveskalume.eventcademy.core.designsystem.theme.Blue200
import com.yveskalume.eventcademy.core.designsystem.theme.ThemePreview
import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.model.EventBooking
import com.yveskalume.eventcademy.core.domain.model.User
import com.yveskalume.eventcademy.core.domain.model.isFuture
import com.yveskalume.eventcademy.core.util.addToPhoneCalendar
import com.yveskalume.eventcademy.core.util.hoursAndMins
import com.yveskalume.eventcademy.core.util.readableDateWithDayName
import com.yveskalume.eventcademy.feature.eventdetail.components.EventBookedDialog

@Composable
fun EventDetailRoute(
    viewModel: EventDetailViewModel = hiltViewModel(),
    eventUid: String?,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventBookingState by viewModel.eventBookingState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var isDialogShown by remember {
        mutableStateOf(false)
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    val uriHandler = LocalUriHandler.current

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    EventDetailUiEffect.ShowCongratulations -> {
                        isDialogShown = true
                    }
                }
            }
        }
    }

    if (isDialogShown) {
        EventBookedDialog(
            onConfirmClick = {
                isDialogShown = false
                with(uiState as? EventDetailUiState.Success) {
                    this?.let {
                        uriHandler.openUri(event.eventUrl)
                    }
                }

            },
            onDismissRequest = { isDialogShown = false }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.getData(eventUid ?: "")
    }

    EventDetailScreen(
        uiState = uiState,
        eventBookingState = eventBookingState,
        onBackClick = onBackClick,
        onOpenLinkClick = { url -> uriHandler.openUri(url) },
        onBookmarkClick = {
            if (uiState is EventDetailUiState.Success) {
                val event = (uiState as EventDetailUiState.Success).event
                if (eventBookingState == EventBookingState.BOOKED) {
                    context.addToPhoneCalendar(
                        eventName = event.name,
                        eventLocation = event.location,
                        eventDescription = event.description,
                        eventStartDate = event.startDate?.time,
                        eventEndDate = event.endDate?.time,
                        eventUrl = event.eventUrl,
                        eventTimezone = event.timezone
                    )
                } else {
                    viewModel.toggleUserEventBooking(
                        event,
                        eventBookingState
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventDetailScreen(
    uiState: EventDetailUiState,
    eventBookingState: EventBookingState,
    onBackClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onOpenLinkClick: (String) -> Unit
) {
    BottomSheetScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Evenement") },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                    ) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                }
            )
        },
        sheetPeekHeight = 120.dp,
        sheetContent = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = if (uiState is EventDetailUiState.Success) {
                        if (uiState.event.price == 0.0) {
                            "Gratuit"
                        } else {
                            "${uiState.event.price}$"
                        }
                    } else {
                        "Prix"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                (uiState as? EventDetailUiState.Success)?.let { state ->
                    if (state.event.eventUrl.isNotBlank()) {
                        IconButton(onClick = { onOpenLinkClick(state.event.eventUrl) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.captive_portal),
                                contentDescription = null
                            )
                        }
                    }
                }

                if (uiState !is EventDetailUiState.Error) {
                    AnimatedContent(targetState = eventBookingState, label = "") { state ->
                        when (state) {
                            EventBookingState.LOADING -> {
                                CircularProgressIndicator()
                            }

                            EventBookingState.NOT_BOOKED -> {
                                if (uiState is EventDetailUiState.Success) {
                                    val isFutureEvent = uiState.event.isFuture
                                    Button(
                                        enabled = isFutureEvent,
                                        shape = RoundedCornerShape(8.dp),
                                        onClick = onBookmarkClick,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text(text = if (isFutureEvent) "J'y vais" else "Passé")
                                    }
                                }
                            }

                            EventBookingState.BOOKED -> {
                                OutlinedButton(
                                    shape = RoundedCornerShape(8.dp),
                                    onClick = onBookmarkClick,
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = MaterialTheme.colorScheme.background
                                    )
                                ) {
                                    Text(text = "Ajouter au calendrier")
                                }
                            }
                        }
                    }
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (uiState) {
                is EventDetailUiState.Error -> {
                    EmptyAnimation(text = uiState.errorMessage)
                }

                EventDetailUiState.Loading -> {
                    LoadingAnimation()
                }

                is EventDetailUiState.Success -> {
                    EventDetailContent(
                        event = uiState.event,
                        organizer = uiState.organizer,
                        bookings = uiState.bookings,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun EventDetailContent(
    event: Event,
    organizer: User?,
    bookings: List<EventBooking>,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()
    val lazyRowState = rememberLazyListState()

    Column(
        modifier = modifier.verticalScroll(state = scrollState)
    ) {
        SubcomposeAsyncImage(
            model = event.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            contentScale = ContentScale.FillWidth
        )

        EventDescriptionSection(
            event = event,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 16.dp, end = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (organizer != null) {
            Text(
                text = "Publié par",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubcomposeAsyncImage(
                    model = organizer.photoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = organizer.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        if (bookings.isNotEmpty()) {
            Text(
                text = "Participants",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            var shownBookingUid: String? by remember {
                mutableStateOf(null)
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                state = lazyRowState
            ) {
                items(items = bookings, key = { it.uid }) { booking ->
                    SubcomposeAsyncImage(
                        model = booking.userPhotoUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                shownBookingUid = if (shownBookingUid != booking.uid) {
                                    booking.uid
                                } else {
                                    null
                                }
                            }
                    )

                    Row(modifier = Modifier.animateContentSize()) {
                        if (shownBookingUid == booking.uid) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = booking.userName)
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(128.dp))
    }
}

@Composable
private fun EventDescriptionSection(
    event: Event,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = event.name,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = event.startDate?.readableDateWithDayName?.uppercase() ?: "",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = Blue200
        )
        SelectionContainer {
            Text(
                text = event.location,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider()
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Fuseau horaire",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = event.timezone,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Début",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = event.startDate?.hoursAndMins ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Fin",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = event.endDate?.hoursAndMins ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Divider()

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Déscription",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(text = event.description)
    }
}

@Preview
@Composable
fun EventDetailScreenPreview() {
    ThemePreview {
        EventDetailScreen(
            eventBookingState = EventBookingState.LOADING,
            uiState = EventDetailUiState.Loading,
            onBackClick = {},
            onBookmarkClick = {},
            onOpenLinkClick = {}
        )
    }
}