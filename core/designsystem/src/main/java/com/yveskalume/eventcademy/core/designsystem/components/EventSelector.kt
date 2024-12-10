package com.yveskalume.eventcademy.core.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.yveskalume.eventcademy.core.domain.model.Event

@Composable
fun EventSelector(
    modifier: Modifier = Modifier,
    value: String,
    eventList: List<Event>,
    showSelector: Boolean,
    onValueChange: (String) -> Unit,
    onIconClicked: () -> Unit,
    onEventClicked: () -> Unit
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth().clickable(
                    onClick = {
                        onIconClicked()
                    }
                ),
            readOnly = true,
            placeholder = {
                Text(text = "Selectionnez un événement")
            },
            value = value,
            onValueChange = { onValueChange },
            trailingIcon = {
                IconButton(onClick = onIconClicked) {
                    Icon(imageVector = Icons.Outlined.KeyboardArrowDown, contentDescription = null)
                }
            }
        )

        DropdownMenu(expanded = showSelector, onDismissRequest = onIconClicked) {
            eventList.forEach {
                DropdownMenuItem(
                    text = {
                        Text(text = it.name)
                    },
                    onClick = {
                        onValueChange(it.name)
                        onEventClicked()
                    }
                )
                Divider()
            }
        }
    }
}