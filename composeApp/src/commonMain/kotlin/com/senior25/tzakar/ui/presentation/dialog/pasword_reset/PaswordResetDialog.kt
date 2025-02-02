package com.senior25.tzakar.ui.presentation.dialog.pasword_reset

import androidx.compose.runtime.Composable
import com.senior25.tzakar.ui.presentation.dialog.base.BaseDialog
import com.senior25.tzakar.ui.theme.MyColors
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.close
import tzakar_reminder.composeapp.generated.resources.password_reset_successfully
import tzakar_reminder.composeapp.generated.resources.success

@Composable
fun showPasswordResetDialog(
    onConfirm:( () -> Unit)? = null,
) {
    BaseDialog(
        title =stringResource(Res.string.success),
        description = stringResource(Res.string.password_reset_successfully),
        okButton = stringResource(Res.string.close),
        okButtonColor = MyColors.colorPurple,
        onConfirm = onConfirm?:{},
        dismiss = false,
    )
}