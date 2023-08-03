package com.yvkalume.eventcademy.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.yvkalume.eventcademy.data.entity.Advertisement
import com.yvkalume.eventcademy.data.entity.fakeAdvertisementList
import com.yvkalume.eventcademy.util.ThemePreview
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdvertisementsItem(
    items: List<Advertisement>,
    onItemClick: (Advertisement) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState()

    LaunchedEffect(Unit) {
        delay(2000)
        while (true) {
            val nextPage =
                if (pagerState.currentPage == items.lastIndex) {
                    pagerState.initialPage
                } else {
                    pagerState.currentPage + 1
                }
            pagerState.animateScrollToPage(nextPage)
            delay(2000)
        }
    }

    HorizontalPager(
        pageCount = items.size,
        state = pagerState,
        modifier = Modifier.fillMaxWidth(),
        pageSpacing = 8.dp
    ) { index ->
        val adItem = items[index]
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = { onItemClick(adItem) })
        ) {
            SubcomposeAsyncImage(
                model = adItem.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = adItem.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = adItem.description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.W400,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun AdvertisementsItemPreview() {
    ThemePreview {
        AdvertisementsItem(
            items = fakeAdvertisementList,
            onItemClick = {},
            modifier = Modifier.aspectRatio(1.5f)
        )
    }
}