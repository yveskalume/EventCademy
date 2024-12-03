package com.yveskalume.eventcademy.feature.createBlogPost

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.yveskalume.eventcademy.core.designsystem.components.SelectDateDialog
import com.yveskalume.eventcademy.core.domain.model.Post
import com.yveskalume.eventcademy.core.util.isValidUrl
import com.yveskalume.eventcademy.feature.createevent.CreateEventUiState
import java.util.Date
import java.util.UUID

@Composable
fun CreatePostRoute(
    onBackClick: () -> Unit = {},
    viewModel: CreatePostViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CreatePostScreen(
        onBackClick = onBackClick,
        onSubmit = viewModel::createPost,
        uiState = uiState
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CreatePostScreen(
    onBackClick: () -> Unit = {},
    onSubmit: (Post) -> Unit = {},
    uiState: CreatePostUiState
){
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    var eventName by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var eventLink by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf("") }

    var isFormValid by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis() + 86400000L,
        )

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
        eventDate,
        eventDescription,
        eventLink,
        imageUri
    ) {
        isFormValid = eventName.isNotBlank() &&
                eventName.length >= 3 &&
                eventDate.isNotBlank() &&
                eventDescription.isNotBlank() && eventDescription.length >= 10 &&
                (eventLink.isEmpty() || eventLink.isValidUrl()) &&
                imageUri.isNotBlank()
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is CreatePostUiState.Success -> {
                Toast.makeText(context, "le post a créé avec succès", Toast.LENGTH_SHORT).show()
                onBackClick()
            }

            is CreatePostUiState.Error -> {
                Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
            }

            else -> {
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Nouveau post") },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClick() },
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
                enabled = isFormValid,
                onClick = {
                    try {
                        val post = Post(
                            uid = UUID.randomUUID().toString(),
                            name = eventName,
                            description = eventDescription,
                            imageUrl = imageUri,
                            eventUrl = eventLink,
                            userUid = "",
                            createdAt = Date(),
                            updatedAt = Date(),
                        )
                        onSubmit(post)
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
                    AnimatedVisibility(visible = false) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                    Text(text = "Soumettre")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CreatePostPreview(){
    //CreatePostScreen()
}