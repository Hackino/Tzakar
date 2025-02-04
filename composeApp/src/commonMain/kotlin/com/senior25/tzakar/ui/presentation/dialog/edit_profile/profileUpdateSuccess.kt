package com.senior25.tzakar.ui.presentation.dialog.edit_profile

import androidx.compose.runtime.Composable
import com.senior25.tzakar.ui.presentation.dialog.base.BaseDialog
import com.senior25.tzakar.ui.theme.MyColors
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources._confirm
import tzakar_reminder.composeapp.generated.resources.close
import tzakar_reminder.composeapp.generated.resources.success
import tzakar_reminder.composeapp.generated.resources.profile_updated_successfully
import tzakar_reminder.composeapp.generated.resources.save_changes
import tzakar_reminder.composeapp.generated.resources.are_you_sure_you_want_save_changes
import tzakar_reminder.composeapp.generated.resources.no

@Composable
fun ShowProfileUpdateSuccessDialog(onDismiss: () -> Unit = {}) {
    BaseDialog(
        title = stringResource(Res.string.success),
        description = stringResource(Res.string.profile_updated_successfully),
        okButton = stringResource(Res.string.close),
        dismiss = false,
        onDismiss = onDismiss,
    )
}


@Composable
fun ShowSaveProfileConfirmation(
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BaseDialog(
        okButtonColor = MyColors.colorRed,
        title = stringResource(Res.string.save_changes),
        description = stringResource(Res.string.are_you_sure_you_want_save_changes),
        okButton = stringResource(Res.string._confirm),
        cancelButton = stringResource(Res.string.no),
        onConfirm = onConfirm,
        dismiss = true,
        onDismiss = onDismiss,
    )
}

