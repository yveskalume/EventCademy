package com.yveskalume.eventcademy.feature.forumhome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yveskalume.eventcademy.core.designsystem.components.LoadingAnimation
import com.yveskalume.eventcademy.core.designsystem.components.PostItem
import com.yveskalume.eventcademy.core.domain.model.Post
import com.yveskalume.eventcademy.feature.postdetail.PostDetailsUiState
import com.yveskalume.eventcademy.feature.postdetail.PostDetailsViewModel
import com.yveskalume.eventcademy.feature.postdetail.PostLikesState


@Composable
fun BlogHomeRoute(
    onCreatePostClick:() -> Unit = {},
    onPostClicked: (String)-> Unit = {},
    viewModel: BlogHomeViewModel =  hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BlogHomeScreen(
        uiState = uiState,
        onCreatePostClick = onCreatePostClick,
        onPostClicked = onPostClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogHomeScreen(
    uiState: BlogHomeUiState,
    onPostClicked: (String)-> Unit = {},
    onCreatePostClick: () -> Unit = {},
){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Rapports d'événements")
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onCreatePostClick() },
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                    )
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when(uiState){
                    BlogHomeUiState.Empty -> {
                        Text(text = "Aucun rapport d'événement pour le moment")
                    }

                    is BlogHomeUiState.Error -> {
                        Text(text = "Une erreur est survenue")
                    }

                    BlogHomeUiState.Loading -> {
                        LoadingAnimation()
                    }

                    is BlogHomeUiState.Success -> {
                        BlogHomeContent(
                            posts = uiState.posts,
                            onPostClicked = onPostClicked
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BlogHomeContent(
    modifier: Modifier = Modifier,
    posts: List<Post> = emptyList(),
    onPostClicked: (String) -> Unit = {},
){
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(posts){post->
            PostItem(
                post = post,
                onPostClicked = {
                    onPostClicked(post.uid)
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BlogHomeScreenPreview(){
    //BlogHomeScreen()
}