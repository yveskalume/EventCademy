package com.yveskalume.eventcademy.feature.createevent

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.yveskalume.eventcademy.core.designsystem.components.SelectDateDialog
import com.yveskalume.eventcademy.core.designsystem.components.SelectTimeDialog
import com.yveskalume.eventcademy.core.designsystem.theme.ThemePreview
import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.model.EventType
import com.yveskalume.eventcademy.core.util.isValidUrl
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun CreateEventRoute(onBackClick: () -> Unit, viewModel: CreateEventViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CreateEventScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onSubmit = viewModel::createEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun CreateEventScreen(
    uiState: CreateEventUiState,
    onBackClick: () -> Unit,
    onSubmit: (Event) -> Unit
) {

    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    var eventName by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventStartTime by remember { mutableStateOf("__:__") }
    var eventEndTime by remember { mutableStateOf("__:__") }
    var eventDescription by remember { mutableStateOf("") }
    var eventPrice by remember { mutableStateOf("0") }
    var eventType: EventType? by remember { mutableStateOf(null) }
    var eventLink by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf("") }

    var isDropdownExpanded by remember { mutableStateOf(false) }
    val dropdownIconRotation by animateFloatAsState(
        label = "",
        targetValue = if (isDropdownExpanded) 180f else 0f
    )

    var isFormValid by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis() + 86400000L)

    val startTimeState = rememberTimePickerState(is24Hour = true)
    val endTimeState = rememberTimePickerState(is24Hour = true)

    val pickMediaLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUri = uri.toString()
            }
        }

    fun pickMedia() {
        pickMediaLauncher.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.SingleMimeType("image/*")
            )
        )
    }

    LaunchedEffect(
        eventName,
        eventLocation,
        eventDate,
        eventStartTime,
        eventEndTime,
        eventDescription,
        eventPrice,
        eventType,
        eventLink,
        imageUri
    ) {
        isFormValid = eventName.isNotBlank() &&
                eventName.length >= 3 &&
                eventLocation.isNotBlank() &&
                eventLocation.length >= 3 &&
                eventDate.isNotBlank() &&
                eventStartTime.isNotBlank() &&
                eventEndTime.isNotBlank() &&
                eventDescription.isNotBlank() && eventDescription.length >= 10 &&
                eventPrice.isNotBlank() &&
                eventType != null &&
                (eventLink.isEmpty() || eventLink.isValidUrl()) &&
                imageUri.isNotBlank()
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is CreateEventUiState.Success -> {
                Toast.makeText(context, "Événement créé avec succès", Toast.LENGTH_SHORT).show()
                onBackClick()
            }

            is CreateEventUiState.Error -> {
                Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
            }

            else -> {
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Nouvel événement") },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                    ) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { contentPadding ->

        SelectDateDialog(
            isVisible = showDatePicker,
            state = datePickerState,
            onDateSelected = {
                eventDate = it
                showDatePicker = false
                focusManager.clearFocus()
            }
        )

        SelectTimeDialog(
            isVisible = showStartTimePicker,
            state = startTimeState,
            onCancel = { showStartTimePicker = false },
            onConfirm = {
                eventStartTime = it
                showStartTimePicker = false
                focusManager.clearFocus()
            }
        )

        SelectTimeDialog(
            isVisible = showEndTimePicker,
            state = endTimeState,
            onCancel = { showEndTimePicker = false },
            onConfirm = {
                eventEndTime = it
                showEndTimePicker = false
                focusManager.clearFocus()
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(contentPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = eventName,
                onValueChange = { eventName = it },
                label = { Text(text = "Nom de l'événement") }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = eventLocation,
                onValueChange = { eventLocation = it },
                label = { Text(text = "Lieu de l'événement") },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null
                    )
                }
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                ) {
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = {
                            focusManager.clearFocus()
                            isDropdownExpanded = false
                        }
                    ) {
                        EventType.values().forEach { type ->
                            DropdownMenuItem(
                                modifier = Modifier.fillMaxWidth(),
                                text = { Text(text = type.name) },
                                onClick = {
                                    eventType = type
                                    focusManager.clearFocus()
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                if (it.isFocused) {
                                    isDropdownExpanded = true
                                }
                            },
                        value = eventType?.name ?: "",
                        onValueChange = { },
                        label = { Text(text = "Type") },
                        singleLine = true,
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.rotate(dropdownIconRotation),
                                imageVector = Icons.Rounded.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f),
                    value = eventPrice,
                    onValueChange = { eventPrice = it },
                    label = { Text(text = "Prix") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.AttachMoney,
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) {
                            showDatePicker = true
                        }
                    },
                value = eventDate,
                readOnly = true,
                onValueChange = { eventDate = it },
                label = { Text(text = "Date de l'événement") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.DateRange,
                        contentDescription = null
                    )
                }
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged {
                            if (it.isFocused) {
                                showStartTimePicker = true
                            }
                        },
                    readOnly = true,
                    value = eventStartTime,
                    onValueChange = { eventStartTime = it },
                    label = { Text(text = "Heure de début") }
                )
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged {
                            if (it.isFocused) {
                                showEndTimePicker = true
                            }
                        },
                    readOnly = true,
                    value = eventEndTime,
                    onValueChange = { eventEndTime = it },
                    label = { Text(text = "Heure de fin") }
                )
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .defaultMinSize(minHeight = 100.dp),
                value = eventDescription,
                onValueChange = { eventDescription = it },
                label = { Text(text = "Description de l'événement") }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = eventLink,
                onValueChange = { eventLink = it },
                label = { Text(text = "Lien de l'événement (facultatif)") }
            )

            AnimatedContent(targetState = imageUri, label = "") { uri ->
                if (uri.isNotBlank()) {
                    SubcomposeAsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = ::pickMedia),
                        contentScale = ContentScale.FillWidth
                    )
                } else {
                    IconButton(
                        onClick = ::pickMedia
                    ) {
                        Icon(imageVector = Icons.Rounded.Image, contentDescription = null)
                    }
                }
            }

            Button(
                enabled = isFormValid && uiState !is CreateEventUiState.Loading,
                onClick = {

                    try {
                        val startDate = Calendar.getInstance()
                        startDate.time = Date(datePickerState.selectedDateMillis!!)
                        startDate.set(Calendar.HOUR_OF_DAY, startTimeState.hour)
                        startDate.set(Calendar.MINUTE, startTimeState.minute)

                        val endDate = Calendar.getInstance()

                        endDate.time = Date(datePickerState.selectedDateMillis!!)
                        endDate.set(Calendar.HOUR_OF_DAY, endTimeState.hour)
                        endDate.set(Calendar.MINUTE, endTimeState.minute)

                        val timeZone: String =
                            SimpleDateFormat("z", Locale.getDefault()).format(startDate.time)

                        if (endDate.before(startDate)) {
                            Toast.makeText(
                                context,
                                "L'heure du début doit être antérieure à l'heure de la fin",
                                Toast.LENGTH_LONG
                            ).show()
                            return@Button
                        }

                        val event = Event(
                            uid = UUID.randomUUID().toString(),
                            name = eventName,
                            description = eventDescription,
                            location = eventLocation,
                            price = eventPrice.toDoubleOrNull() ?: 0.0,
                            startDate = startDate.time,
                            endDate = endDate.time,
                            imageUrl = imageUri,
                            eventUrl = eventLink,
                            type = eventType ?: EventType.OFFLINE,
                            userUid = "",
                            timezone = timeZone,
                            createdAt = Date(),
                            updatedAt = Date(),
                        )
                        onSubmit(event)
                    } catch (e: Exception) {
                        Log.e("CreateEventScreen", "CreateEventScreen: ${e.message}")
                        Toast.makeText(
                            context,
                            "Une erreur est survenue",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    AnimatedVisibility(visible = uiState is CreateEventUiState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                    Text(text = "Soumettre")
                }
            }
        }
    }
}

@Preview
@Composable
fun CreateEventScreenPreview() {
    ThemePreview {
        CreateEventScreen(uiState = CreateEventUiState.Initial, onBackClick = {}, onSubmit = {})
    }
}