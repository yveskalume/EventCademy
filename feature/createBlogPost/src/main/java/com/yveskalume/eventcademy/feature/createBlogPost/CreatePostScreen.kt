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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.yveskalume.eventcademy.core.designsystem.components.EventSelector
import com.yveskalume.eventcademy.core.domain.model.Post
import com.yveskalume.eventcademy.feature.createevent.CreateEventUiState
import com.yveskalume.eventcademy.feature.home.HomeUiState
import com.yveskalume.eventcademy.feature.home.HomeViewModel
import java.util.Date
import java.util.UUID

@Composable
fun CreatePostRoute(
    onBackClick: () -> Unit = {},
    viewModel: CreatePostViewModel = hiltViewModel(),
    eventViewModel: HomeViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventUIState by eventViewModel.uiState.collectAsStateWithLifecycle()

    CreatePostScreen(
        onBackClick = onBackClick,
        onSubmit = viewModel::createPost,
        uiState = uiState,
        eventUIState = eventUIState
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CreatePostScreen(
    onBackClick: () -> Unit = {},
    onSubmit: (Post) -> Unit = {},
    uiState: CreatePostUiState,
    eventUIState: HomeUiState
){
    val context = LocalContext.current

    var eventDescription by remember { mutableStateOf("") }
    var imageUris  by remember { mutableStateOf<List<String>>(emptyList()) }
    var evenName by remember { mutableStateOf("") }
    var showEventSelector by remember { mutableStateOf(false)}
    val events = eventUIState as? HomeUiState.Success
    var isFormValid by remember { mutableStateOf(false) }

    val pickMediaLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uri ->
            if (uri.isNotEmpty()) {
                val imageList = mutableListOf<String>()
                uri.forEach{
                    imageList.add(it.toString())
                }
                imageUris = imageList

                Log.d("CreatePostScreen", "imageUris: $imageUris")
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
        eventDescription,
        imageUris
    ) {
        isFormValid =
                eventDescription.isNotBlank() && eventDescription.length >= 10 &&
                imageUris.isNotEmpty()
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
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(contentPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            EventSelector(
                value = evenName,
                showSelector = showEventSelector,
                onValueChange = { name-> evenName = name },
                onIconClicked = { showEventSelector = !showEventSelector },
                onEventClicked = { showEventSelector = !showEventSelector },
                eventList = events?.pastEvents ?: emptyList(),
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

            AnimatedContent(targetState = imageUris, label = "") { imageUris ->
                if (imageUris.isNotEmpty()) {
                    LazyRow {
                        items(imageUris){uri->
                            SubcomposeAsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .width(300.dp)
                                    .padding(10.dp)
                                    .clickable(onClick = ::pickMedia),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
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
                            postContent = eventDescription,
                            imageUrls = imageUris,
                            eventName = evenName,
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
                    AnimatedVisibility(visible = uiState is CreatePostUiState.Loading) {
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