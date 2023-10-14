package com.yveskalume.eventcademy.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTimeDialog(
    isVisible: Boolean,
    state: TimePickerState,
    onCancel: () -> Unit,
    onConfirm: (String) -> Unit
) {

    if (isVisible) {
        AlertDialog(
            onDismissRequest = onCancel
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TimePicker(state = state)
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(
                        onClick = {
                            val minutes = if (state.minute < 10) "0${state.minute}" else state.minute
                            val hours = if (state.hour < 10) "0${state.hour}" else state.hour
                            onConfirm("$hours:$minutes")
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Confirmer")
                    }
                }
            }
        }
    }

}