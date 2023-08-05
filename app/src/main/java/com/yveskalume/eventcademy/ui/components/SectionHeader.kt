package com.yveskalume.eventcademy.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.yveskalume.eventcademy.util.ThemePreview

@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
fun SectionHeaderPreview() {
    ThemePreview {
        SectionHeader(title = "A ne pas manquer")
    }
}