package com.yveskalume.eventcademy.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.yveskalume.eventcademy.core.designsystem.util.shimmer
import com.yveskalume.eventcademy.core.domain.model.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    modifier: Modifier = Modifier,
    post: Post,
    onPostClicked: () -> Unit = {}
) {
    OutlinedCard(
        modifier = modifier
            .clickable(
                onClick = onPostClicked
            ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            0.1.dp,
            Color.Gray
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SubcomposeAsyncImage(
                model = post.imageUrls[0],
                contentDescription = null,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .shimmer()
                    )
                },
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Text(
                text = if (post.eventName != null) post.eventName.toString() else post.postContent,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

