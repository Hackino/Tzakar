package com.senior25.tzakar.ui.presentation.screen.main.edit_profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.fields.EmailField
import com.senior25.tzakar.ui.presentation.components.fields.PasswordField
import com.senior25.tzakar.ui.presentation.components.fields.userNameField
import com.senior25.tzakar.ui.presentation.components.loader.FullScreenLoader
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar
import com.senior25.tzakar.ui.presentation.dialog.edit_profile.ShowProfileUpdateSuccessDialog
import com.senior25.tzakar.ui.presentation.dialog.edit_profile.ShowSaveProfileConfirmation
import com.senior25.tzakar.ui.theme.MyColors
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.edit_profile
import tzakar_reminder.composeapp.generated.resources.email_address
import tzakar_reminder.composeapp.generated.resources.enter_email_address
import tzakar_reminder.composeapp.generated.resources.enter_password
import tzakar_reminder.composeapp.generated.resources.enter_username
import tzakar_reminder.composeapp.generated.resources.ic_email
import tzakar_reminder.composeapp.generated.resources.ic_lock
import tzakar_reminder.composeapp.generated.resources.ic_person
import tzakar_reminder.composeapp.generated.resources.password
import tzakar_reminder.composeapp.generated.resources.save_changes
import tzakar_reminder.composeapp.generated.resources.username

class EditProfileScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel = koinScreenModel<EditProfileViewModel>()

        val interaction= object :EditProfilePageInteraction{
            override fun onContinueClick() {
//                   screenModel.updateProfile().collectLatest {
//                       onUIEvent(EditProfilePageEvent.Success)
//                       onUIEvent(EditProfilePageEvent.UpdatePopUpState(EditProfilePagePopUp.SaveChangesSuccess))
//                   }
            }

            override fun onUIEvent(event: EditProfilePageEvent) {
                screenModel.onUIEvent(event)
            }

            override fun getUiState(): StateFlow<EditProfilePageUiState?> = screenModel.uiState

            override fun getUsername(): String? = screenModel.username

            override fun getEmail(): String? = screenModel.email

            override fun getPopupState(): StateFlow<EditProfilePagePopUp?> =screenModel.popUpState

            override fun onBackPress() {
                navigator.pop()
            }
        }

        Scaffold(
            topBar = { MyTopAppBar( stringResource(Res.string.edit_profile) , showBack = true,interaction= interaction) },
            content = {paddingValues ->  EditProfilePageScreen(paddingValues,interaction) },
        )
    }
}


@Composable
fun EditProfilePageScreen(paddingValues: PaddingValues,interaction: EditProfilePageInteraction?) {

    var isValidUsername by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val usernameFocusRequester = remember { FocusRequester() }
    val uiState =  interaction?.getUiState()?.collectAsState()

    val popUpState = interaction?.getPopupState()?.collectAsState()

    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp).padding(bottom = 50.dp),
        shape =  RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MyColors.colorLightGrey,),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MyColors.colorOffWhite)
                .padding(top = 16.dp)
                .padding(bottom = paddingValues.calculateBottomPadding())
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                userNameField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    label = stringResource(Res.string.username),
                    placeHolder = stringResource(Res.string.enter_username),
                    value = interaction?.getUsername(),
                    onValueChange = { interaction?.onUIEvent(EditProfilePageEvent.UpdateUserName(it)) },
                    isInputValid = { isValidUsername = it },
                    imeAction = ImeAction.Done,
                    focusRequester = usernameFocusRequester,
                    leadingIcon = painterResource(Res.drawable.ic_person),
                    onKeyPressed = {
                        usernameFocusRequester.freeFocus();
                        keyboardController?.hide()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                EmailField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    label = stringResource(Res.string.email_address),
                    placeHolder = stringResource(Res.string.enter_email_address),
                    value = interaction?.getEmail(),
                    imeAction = ImeAction.Next,
                    leadingIcon = painterResource(Res.drawable.ic_email),
                    isEnabled = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    label = stringResource(Res.string.password),
                    placeHolder = stringResource(Res.string.enter_password),
                    value = "*********",
                    imeAction = ImeAction.Done,
                    leadingIcon = painterResource(Res.drawable.ic_lock),
                    trailingIcon = null,
                    isEnabled = false
                )
            }

            Column(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomButton(
                    isEnabled = isValidUsername,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    onClick = { interaction?.onUIEvent(EditProfilePageEvent.UpdatePopUpState(EditProfilePagePopUp.SaveChanges)) },
                    text = stringResource(Res.string.save_changes)
                )
            }
        }
    }
    if (uiState?.value is EditProfilePageUiState.ProgressLoader) FullScreenLoader()

    if (popUpState?.value is EditProfilePagePopUp.SaveChanges){
        ShowSaveProfileConfirmation(
            onConfirm = { interaction.onContinueClick() },
            onDismiss = { interaction?.onUIEvent(EditProfilePageEvent.UpdatePopUpState(EditProfilePagePopUp.None)) }
        )
    }

    if (popUpState?.value is EditProfilePagePopUp.SaveChangesSuccess) {
        ShowProfileUpdateSuccessDialog(
            onDismiss = { interaction?.onBackPress() }
        )
    }
}

interface EditProfilePageInteraction: BackPressInteraction {
    fun onContinueClick()
    fun onUIEvent(event: EditProfilePageEvent)
    fun getUiState(): StateFlow<EditProfilePageUiState?>
    fun getUsername():String?
    fun getEmail():String?
    fun getPopupState(): StateFlow<EditProfilePagePopUp?>
}
