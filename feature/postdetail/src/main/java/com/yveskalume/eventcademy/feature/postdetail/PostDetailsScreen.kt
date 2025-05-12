package com.yveskalume.eventcademy.feature.postdetail

import android.widget.Toast
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.yveskalume.eventcademy.core.designsystem.components.EmptyAnimation
import com.yveskalume.eventcademy.core.designsystem.components.LoadingAnimation
import com.yveskalume.eventcademy.core.domain.model.Post
import com.yveskalume.eventcademy.core.domain.model.PostLike
import com.yveskalume.eventcademy.core.domain.model.User

@Composable
fun PostDetailsRoute(
    viewModel: PostDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val postLikesState by viewModel.postLikesState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    PostDetailsScreen(
        uiState = uiState,
        likesState = postLikesState,
        onLikeClick = {
            if (uiState is PostDetailsUiState.Success){
                val post = (uiState as PostDetailsUiState.Success).post
                if (postLikesState == PostLikesState.LIKED){
                    Toast.makeText(context, "Vous aimez déjà ce post", Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.toggleUserPostLIke(
                        post,
                        postLikesState
                    )
                }
            }
        },
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsScreen(
    uiState: PostDetailsUiState,
    likesState: PostLikesState,
    onBackClick: () -> Unit,
    onLikeClick: () -> Unit,
){
    BottomSheetScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Details du Post") },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        sheetPeekHeight = 120.dp,
        sheetContent = {
        }
    ) {
        Box(
            modifier = Modifier
            .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            when(uiState){
                is PostDetailsUiState.Error -> {
                    EmptyAnimation(text = uiState.errorMessage)
                }


                PostDetailsUiState.Loading ->{
                    LoadingAnimation()
                }

                is PostDetailsUiState.Success ->{
                    PostDetailsContent(
                        post = uiState.post,
                        publisher = uiState.publisher,
                        likes = uiState.likes,
                        modifier = Modifier
                            .fillMaxSize(),
                        onLikeClick = onLikeClick,
                        likeState = likesState
                    )
                }

            }
        }
    }
}

@Composable
fun PostDetailsContent(
    post: Post,
    publisher: User?,
    likes: List<PostLike>,
    modifier: Modifier = Modifier,
    onLikeClick: () -> Unit,
    likeState: PostLikesState
){
    val scrollState = rememberScrollState()
    val lazyRowState = rememberLazyListState()

    Column(
        modifier = modifier
            .verticalScroll(state = scrollState)
    ) {

        if (publisher != null){
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
                    model = publisher.photoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = publisher.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        PostDescriptionSection(
            post = post,
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(start = 16.dp, end = 16.dp)
        )


        LazyRow(
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ){

            items(post.imageUrls){image->
                SubcomposeAsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .animateContentSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            OutlinedButton(
                onClick = onLikeClick,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Icon(
                        imageVector = if (likeState == PostLikesState.LIKED) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = null,
                        tint = if (likeState == PostLikesState.LIKED) Color.Red else Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${likes.size}"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (likes.isNotEmpty()){
            Text(
                text = "Aimé par",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            var shownLikeUid: String? by remember {
                mutableStateOf(null)
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                state = lazyRowState
            ) {
                items(items = likes, key = { it.uid }) { like ->
                    SubcomposeAsyncImage(
                        model = like.userPhotoUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                shownLikeUid = if (shownLikeUid != like.uid) {
                                    like.uid
                                } else {
                                    null
                                }
                            }
                    )

                    Row(modifier = Modifier.animateContentSize()) {
                        if (shownLikeUid == like.uid) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = like.userName)
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
private fun PostDescriptionSection(
    modifier: Modifier = Modifier,
    post: Post
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        if (post.eventName != null){
            Text(
                text = post.eventName!!,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider()


            Spacer(modifier = Modifier.height(4.dp))
        }

        Text(
            text = post.postContent,
        )

    }
}