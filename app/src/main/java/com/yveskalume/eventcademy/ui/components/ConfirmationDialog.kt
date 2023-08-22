package com.yveskalume.eventcademy.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.yveskalume.eventcademy.ui.theme.EventCademyTheme

@Composable
fun ConfirmationDialog(
    isVisible: Boolean,
    title: String,
    description: String,
    onCancel: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onConfirm,
            title = {
                Text(text = title)
            },
            text = {
                Text(text = description)
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirm
                ) {
                    Text("Confirme")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onCancel
                ) {
                    Text("Annuler")
                }
            }
        )
    }
}

@Preview
@Composable
fun ConfirmationDialogPreview() {
    EventCademyTheme {
        ConfirmationDialog(
            isVisible = true,
            title = "Supprimer l'évènement",
            description = "Voulez-vous vraiment supprimer cet évènement ?",
            onCancel = {},
            onConfirm = {}
        )
    }
}