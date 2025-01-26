package com.senior25.tzakar.ui.presentation.dialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import com.senior25.tzakar.ui.theme.MyColors
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.failed


@Composable
fun ShowDialog(
    title: String = stringResource(Res.string.failed),
    message: String,
    onConfirm:( () -> Unit)? = null,
    onDismiss:( () -> Unit)? = null,
    confirmText:String? =  "",
    closeText:String? =  ""
) {
    AlertDialog(
        onDismissRequest = { onDismiss?.invoke() },
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = { onConfirm?.invoke() }) {
                Text(confirmText?:"", color = MyColors.colorPurple)
            }
        },
        dismissButton =onDismiss?.let { {
                TextButton(onClick = onDismiss) {
                    Text(closeText?:"")
                }
            }
        }
    )
}