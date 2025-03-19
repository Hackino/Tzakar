package com.senior25.tzakar.ui.presentation.dialog.reminder_set

import androidx.compose.runtime.Composable
import com.senior25.tzakar.ui.presentation.dialog.base.BaseDialog
import com.senior25.tzakar.ui.theme.MyColors
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources._confirm
import tzakar_reminder.composeapp.generated.resources.alert
import tzakar_reminder.composeapp.generated.resources.are_you_sure_you_want_save_changes
import tzakar_reminder.composeapp.generated.resources.are_you_you_sure_you_want_to_set_this_reminder
import tzakar_reminder.composeapp.generated.resources.close
import tzakar_reminder.composeapp.generated.resources.no
import tzakar_reminder.composeapp.generated.resources.profile_updated_successfully
import tzakar_reminder.composeapp.generated.resources.reminder_set_successfully
import tzakar_reminder.composeapp.generated.resources.save_changes
import tzakar_reminder.composeapp.generated.resources.select_valid_date_before
import tzakar_reminder.composeapp.generated.resources.set_reminder
import tzakar_reminder.composeapp.generated.resources.success


@Composable
fun ShowSelectValidDate(onDismiss: () -> Unit = {}) {
    BaseDialog(
        title = stringResource(Res.string.alert),
        description = stringResource(Res.string.select_valid_date_before),
        okButton = stringResource(Res.string.close),
        dismiss = false,
        onDismiss = onDismiss,
    )
}



@Composable
fun ShowReminderAddedSuccessDialog(onDismiss: () -> Unit = {}) {
    BaseDialog(
        title = stringResource(Res.string.success),
        description = stringResource(Res.string.profile_updated_successfully),
        okButton = stringResource(Res.string.close),
        dismiss = false,
        onDismiss = onDismiss,
    )
}


@Composable
fun ShowAddReminderConfirmation(
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BaseDialog(
        okButtonColor = MyColors.colorPurple,
        title = stringResource(Res.string.set_reminder),
        description = stringResource(Res.string.are_you_you_sure_you_want_to_set_this_reminder),
        okButton = stringResource(Res.string._confirm),
        cancelButton = stringResource(Res.string.no),
        onConfirm = onConfirm,
        dismiss = true,
        onDismiss = onDismiss,
    )
}
