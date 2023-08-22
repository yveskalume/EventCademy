package com.yveskalume.eventcademy.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.yveskalume.eventcademy.data.entity.Event
import com.yveskalume.eventcademy.data.entity.statusText
import com.yveskalume.eventcademy.ui.theme.Blue200
import com.yveskalume.eventcademy.ui.theme.EventCademyTheme
import com.yveskalume.eventcademy.util.readableDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventPublishedItem(event: Event,onClick: () -> Unit,onDeleteClick: () -> Unit, modifier: Modifier = Modifier) {

    val eventStatusColor by animateColorAsState(
        targetValue = if (event.rejected) {
            MaterialTheme.colorScheme.error
        } else if (event.published) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.Yellow
        }, label = ""
    )

    Card(onClick = onClick, modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .weight(1.5f)
                    .aspectRatio(1.2f),
                model = event.imageUrl,
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .weight(2.5f)
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = event.statusText,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(eventStatusColor.copy(alpha = 0.3f))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.clickable(onClick = onDeleteClick)
                    )
                }
                Text(
                    text = event.name, style =
                    MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = event.startDate?.readableDate ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Blue200,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = event.location, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Preview
@Composable
fun EventPublishedItemPreview() {
    EventCademyTheme {
        EventPublishedItem(event = Event(), onClick = {}, onDeleteClick = {})
    }
}