package com.yveskalume.eventcademy.ui.screen.createevent

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yveskalume.eventcademy.data.entity.EventType
import com.yveskalume.eventcademy.ui.components.SelectDateDialog
import com.yveskalume.eventcademy.util.ThemePreview
import com.yveskalume.eventcademy.util.isValidUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(onBackClick: () -> Unit) {

    var eventName by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventStartTime by remember { mutableStateOf("") }
    var eventEndTime by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var eventPrice by remember { mutableStateOf("") }
    var eventType: EventType? by remember { mutableStateOf(null) }
    var eventLink by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf("") }

    val dateFocusRequester = remember { FocusRequester() }
    val typeFocusRequester = remember { FocusRequester() }

    val startTimeFocus = remember { FocusRequester() }
    var priceFocus by remember { mutableStateOf(FocusRequester()) }


    var isDropdownExpanded by remember { mutableStateOf(false) }
    val dropdownIconRotation by animateFloatAsState(
        label = "",
        targetValue = if (isDropdownExpanded) 180f else 0f
    )

    var isFormValid by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis() + 86400000L)


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
                startTimeFocus.requestFocus()
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
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
                            priceFocus.requestFocus()
                            isDropdownExpanded = false
                        }
                    ) {
                        EventType.values().forEach { type ->
                            DropdownMenuItem(
                                modifier = Modifier.fillMaxWidth(),
                                text = { Text(text = type.name) },
                                onClick = {
                                    eventType = type
                                    isDropdownExpanded = false
                                    priceFocus.requestFocus()
                                }
                            )
                        }
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(typeFocusRequester)
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
                    modifier = Modifier.weight(1f).focusRequester(priceFocus),
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
                    .focusRequester(dateFocusRequester)
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
                    modifier = Modifier.weight(1f).focusRequester(startTimeFocus),
                    value = eventStartTime,
                    onValueChange = { eventStartTime = it },
                    label = { Text(text = "Heure de début") }
                )
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
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

            Button(
                enabled = isFormValid,
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Soumettre")
            }
        }
    }
}

@Preview
@Composable
fun CreateEventScreenPreview() {
    ThemePreview {
        CreateEventScreen(onBackClick = {})
    }
}