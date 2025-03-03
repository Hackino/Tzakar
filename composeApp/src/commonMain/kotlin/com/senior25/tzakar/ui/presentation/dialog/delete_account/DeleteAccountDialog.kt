package com.senior25.tzakar.ui.presentation.dialog.delete_account

import androidx.compose.runtime.Composable
import com.senior25.tzakar.ui.presentation.dialog.base.BaseDialog
import com.senior25.tzakar.ui.theme.MyColors
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources._confirm
import tzakar_reminder.composeapp.generated.resources.are_you_sure_you_want_logout
import tzakar_reminder.composeapp.generated.resources.are_you_sure_you_want_to_delete_your_account
import tzakar_reminder.composeapp.generated.resources.delete_account
import tzakar_reminder.composeapp.generated.resources.logout
import tzakar_reminder.composeapp.generated.resources.no



@Composable
fun showDeleteAccountDialog(
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BaseDialog(
        okButtonColor = MyColors.colorRed,
        title = stringResource(Res.string.delete_account),
        description = stringResource(Res.string.are_you_sure_you_want_to_delete_your_account),
        okButton = stringResource(Res.string._confirm),
        cancelButton = stringResource(Res.string.no),
        onConfirm = onConfirm,
        dismiss = true,
        onDismiss = onDismiss,
    )
}

