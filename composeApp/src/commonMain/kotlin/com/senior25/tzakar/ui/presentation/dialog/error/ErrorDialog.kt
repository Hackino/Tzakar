package com.senior25.tzakar.ui.presentation.dialog.error

import androidx.compose.runtime.Composable
import com.senior25.tzakar.ui.presentation.dialog.base.BaseDialog
import com.senior25.tzakar.ui.theme.MyColors
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.failed


@Composable
fun ShowErrorDialog(
    title: String = stringResource(Res.string.failed),
    message: String,
    onConfirm:( () -> Unit)? = null,
    onDismiss: () -> Unit = {},
    confirmText:String? =  "",
) {
    BaseDialog(
        title =title,
        description = message,
        okButton = confirmText,
        okButtonColor = MyColors.colorPurple,
        onConfirm = onConfirm?:{},
        dismiss = true,
        onDismiss = onDismiss,
    )
}

