package com.yveskalume.eventcademy.ui.screen.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.yveskalume.eventcademy.data.entity.Event
import com.yveskalume.eventcademy.data.entity.User
import com.yveskalume.eventcademy.data.entity.UserRole
import com.yveskalume.eventcademy.ui.components.ConfirmationDialog
import com.yveskalume.eventcademy.ui.components.EmptyAnimation
import com.yveskalume.eventcademy.ui.components.EventPublishedItem
import com.yveskalume.eventcademy.ui.components.LoadingAnimation
import com.yveskalume.eventcademy.ui.theme.Blue200
import com.yveskalume.eventcademy.ui.theme.PrimaryGreen400
import com.yveskalume.eventcademy.util.ThemePreview

@Composable
fun ProfileRoute(
    onAddEventClick: () -> Unit,
    onEventClick: (String) -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isDeletingEvent by viewModel.isEventDeleting.collectAsStateWithLifecycle()

    var eventToDeleteUid by remember {
        mutableStateOf("")
    }

    var isLogoutConfirmationShown by remember {
        mutableStateOf(false)
    }

    ConfirmationDialog(
        isVisible = eventToDeleteUid.isNotBlank(),
        title = "Supprimer l'évènement",
        description = "Voulez-vous vraiment supprimer cet évènement ?",
        onCancel = { eventToDeleteUid = "" },
        onConfirm = {
            viewModel.deleteEvent(eventToDeleteUid)
            eventToDeleteUid = ""
        }
    )

    ConfirmationDialog(
        isVisible = isLogoutConfirmationShown,
        title = "Déconnexion",
        description = "Voulez-vous vraiment vous déconnecter ?",
        onCancel = { isLogoutConfirmationShown = false },
        onConfirm = {
            onLogoutClick()
        }
    )

    ProfileScreen(
        uiState = uiState,
        isDeletingEvent = isDeletingEvent,
        onEventClick = onEventClick,
        onEventDelete = {
            eventToDeleteUid = it
        },
        onAddEventClick = onAddEventClick,
        onLogoutClick = {
            isLogoutConfirmationShown = true
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(
    uiState: ProfileUiState,
    isDeletingEvent: Boolean,
    onEventClick: (String) -> Unit = {},
    onEventDelete: (String) -> Unit,
    onAddEventClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profil") },
                actions = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(
                            imageVector = Icons.Rounded.Logout,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState is ProfileUiState.Success && uiState.user.role == UserRole.ORGANIZER) {
                FloatingActionButton(
                    onClick = onAddEventClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                    )
                }
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = isDeletingEvent, modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            when (uiState) {
                is ProfileUiState.Loading -> {
                    LoadingAnimation()
                }

                is ProfileUiState.Success -> {
                    ProfileContent(
                        modifier = Modifier.fillMaxSize(),
                        user = uiState.user,
                        events = uiState.userEvents,
                        onEventClick = { onEventClick(it) },
                        onEventDeleteClick = { onEventDelete(it) }
                    )
                }

                is ProfileUiState.Error -> {
                    EmptyAnimation(text = "Une erreur est survenue")
                }
            }
        }
    }
}

@Composable
fun ProfileContent(
    user: User,
    events: List<Event>,
    onEventClick: (String) -> Unit,
    onEventDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PhotoSection(user = user, modifier = Modifier.padding(8.dp))
        }

        item {
            Divider(thickness = 1.dp)
        }

        item {
            Text(
                text = "Vos evenements",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        if (events.isEmpty()) {
            item {
                EmptyAnimation(
                    text = "Vous n'avez pas encore publié d'évènement",
                )
            }
        }

        items(events) { event ->
            EventPublishedItem(
                modifier = Modifier.padding(horizontal = 16.dp),
                event = event,
                onClick = { onEventClick(event.uid) },
                onDeleteClick = { onEventDeleteClick(event.uid) },
            )
        }

    }
}

@Composable
private fun PhotoSection(user: User, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = ""
    )
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .size(120.dp)
                .drawBehind {
                    rotate(rotation) {
                        drawCircle(
                            brush = Brush.linearGradient(
                                listOf(
                                    PrimaryGreen400,
                                    Blue200
                                )
                            ),
                            style = Stroke(4.dp.toPx())
                        )
                    }
                }
                .padding(6.dp)
                .clip(CircleShape),
            model = user.photoUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
            text = user.name,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Text(text = user.email, textAlign = TextAlign.Center)
    }
}

@Preview
@Composable
fun SettingScreenPreview() {
    ThemePreview {
        ProfileScreen(
            uiState = ProfileUiState.Success(User(), listOf()),
            isDeletingEvent = true,
            onEventClick = {},
            onEventDelete = {},
            onAddEventClick = {},
            onLogoutClick = {}
        )
    }
}