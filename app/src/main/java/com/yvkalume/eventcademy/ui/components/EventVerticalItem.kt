package com.yvkalume.eventcademy.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yvkalume.eventcademy.R
import com.yvkalume.eventcademy.util.ThemePreview
import kiwi.orbit.compose.icons.Icons
import kiwi.orbit.compose.ui.OrbitTheme
import kiwi.orbit.compose.ui.controls.Icon
import kiwi.orbit.compose.ui.controls.IconButton
import kiwi.orbit.compose.ui.controls.SurfaceCard
import kiwi.orbit.compose.ui.controls.Text

@Composable
fun EventVerticalItem() {
    SurfaceCard(
        onClick = {}, modifier = Modifier
            .size(width = 240.dp, height = 290.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 6.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1.3f)) {
                    Text(
                        text = "Mardi 21 Juillet - 12h00",
                        style = OrbitTheme.typography.bodyNormal
                    )
                    Text(
                        text = "Lorem ipsum Lorem ipsum Lorem ipsum",
                        style = OrbitTheme.typography.title3,
                        maxLines = 2
                    )
                }

                Column(modifier = Modifier.weight(0.7f)) {
                    Text(text = "Soracert", style = OrbitTheme.typography.bodyNormal)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = Icons.Location,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Esisalama", style = OrbitTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(painter = Icons.ShareIos, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventVerticalItemPreview() {
    ThemePreview {
        EventVerticalItem()
    }
}