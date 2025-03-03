package com.senior25.tzakar.ui.presentation.screen.main.change_password

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.debounce.debounceClick
import com.senior25.tzakar.ui.presentation.components.fields.ConfirmPasswordField
import com.senior25.tzakar.ui.presentation.components.fields.PasswordField
import com.senior25.tzakar.ui.presentation.components.loader.FullScreenLoader
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar
import com.senior25.tzakar.ui.presentation.dialog.error.ShowErrorDialog
import com.senior25.tzakar.ui.presentation.dialog.pasword_reset.showPasswordChangedDialog
import com.senior25.tzakar.ui.presentation.dialog.pasword_reset.showPasswordResetDialog
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.EditProfilePageScreen
import com.senior25.tzakar.ui.presentation.screen.registration.forget_password.ForgotPasswordScreen
import com.senior25.tzakar.ui.presentation.screen.registration.reset_password.ResetPasswordActionDialogType
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH1
import com.senior25.tzakar.ui.theme.fontLink
import com.senior25.tzakar.ui.theme.fontParagraphL
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.app_icon
import tzakar_reminder.composeapp.generated.resources.back
import tzakar_reminder.composeapp.generated.resources.change_password
import tzakar_reminder.composeapp.generated.resources.close
import tzakar_reminder.composeapp.generated.resources.confirm_new_password
import tzakar_reminder.composeapp.generated.resources.confirm_password
import tzakar_reminder.composeapp.generated.resources.dont_worry_resetting_your_passowrd_is_easy
import tzakar_reminder.composeapp.generated.resources.edit_profile
import tzakar_reminder.composeapp.generated.resources.enter_password
import tzakar_reminder.composeapp.generated.resources.failed
import tzakar_reminder.composeapp.generated.resources.ic_back
import tzakar_reminder.composeapp.generated.resources.ic_eye_off
import tzakar_reminder.composeapp.generated.resources.ic_lock
import tzakar_reminder.composeapp.generated.resources.new_password
import tzakar_reminder.composeapp.generated.resources.old_password
import tzakar_reminder.composeapp.generated.resources.password
import tzakar_reminder.composeapp.generated.resources.please_enter_new_password
import tzakar_reminder.composeapp.generated.resources.please_enter_old_password
import tzakar_reminder.composeapp.generated.resources.reset_password

class ChangePasswordScreen:Screen {

    @Composable
    override fun Content() {
        val localNavigator = LocalNavigator.currentOrThrow

        val mainViewModel = koinScreenModel<MainScreenViewModel>()
        val viewModel = koinScreenModel<ChangePasswordScreenViewModel>()
        val statusCode = viewModel.errorStatusCode.collectAsState()
        val isLoading = viewModel.isLoading.collectAsState()
        val dialogType = viewModel.dialogType.collectAsState()

       val interaction  =  object :ChangePasswordScreenInteraction{
            override fun getOldPassword():  StateFlow<String> = viewModel.oldPassword
            override fun getPassword():  StateFlow<String> = viewModel.newPassword
            override fun getConfirmPassword():  StateFlow<String> = viewModel.confirmPassword
            override fun onUIEvent(event: ChangePasswordPageEvent) { viewModel.onUIEvent(event) }
            override fun getUiState(): StateFlow<ChangePasswordPageUiState?> = viewModel.uiState
            override fun navigate(action: ChangePasswordAction) {
                when (action) {
                    ChangePasswordAction.CHANGE ->{
                        viewModel.changePassword{
                            viewModel._dialogType.value = ChangePasswordActionDialogType.PASSWORD_CHANGED
                        }
                    }
                    ChangePasswordAction.BACK -> localNavigator.pop()
                }
            }

           override fun onBackPress() {
               localNavigator.pop()
           }
       }

        Scaffold(
            topBar = { MyTopAppBar( stringResource(Res.string.edit_profile) , showBack = true,interaction= interaction) },
            content = {paddingValues ->  ChangePasswordScreen(paddingValues,interaction) },
        )

        statusCode.value?.let {
            ShowErrorDialog(
                title = stringResource(Res.string.failed),
                message = it.errorMessage.toString(),
                onConfirm = { viewModel._errorStatusCode.value = null },
                confirmText = stringResource(Res.string.close)
            )
        }

        when(dialogType.value){
            ChangePasswordActionDialogType.PASSWORD_CHANGED -> {
                showPasswordChangedDialog{
                    localNavigator.pop()
                    viewModel._dialogType.value = null
                }
            }
            null ->Unit
        }

        isLoading.value?.let { FullScreenLoader() }
    }
}

@Composable
private fun ChangePasswordScreen(paddingValues: PaddingValues,interaction: ChangePasswordScreenInteraction? = null) {
    var isPasswordValid by remember { mutableStateOf(false) }
    var isConfirmPasswordValid by remember { mutableStateOf(false) }
    var isOldPasswordValid by remember { mutableStateOf(false) }

    val uiState: State<ChangePasswordPageUiState?>? = interaction?.getUiState()?.collectAsState()
    val oldPassword: State<String>? = interaction?.getOldPassword()?.collectAsState()
    val password: State<String>? = interaction?.getPassword()?.collectAsState()
    val confirmPassword: State<String>? = interaction?.getConfirmPassword()?.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val oldpasswordFocusRequester = remember { FocusRequester() }

    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    Column(
        modifier =  Modifier.fillMaxSize()
            .background(MyColors.colorOffWhite)
            .padding(bottom = 64.dp)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

            Surface(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MyColors.colorLightGrey,),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(Res.string.change_password),
                            color = MyColors.colorDarkBlue,
                            textAlign = TextAlign.Center,
                            style = fontH1.copy(fontSize = 34.sp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        PasswordField(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            label = stringResource(Res.string.old_password),
                            placeHolder = stringResource(Res.string.please_enter_old_password),
                            value = oldPassword?.value ?: "",
                            onValueChange = {
                                interaction?.onUIEvent(ChangePasswordPageEvent.UpdateOldPassword(it))
                            },
                            isInputValid = { isOldPasswordValid = it },
                            imeAction = ImeAction.Next,
                            focusRequester = oldpasswordFocusRequester,
                            onKeyPressed = { passwordFocusRequester.requestFocus() },
                            leadingIcon = painterResource(Res.drawable.ic_lock),
                            trailingIcon = painterResource(Res.drawable.ic_eye_off),
                            fullValidation = false
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        PasswordField(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            label = stringResource(Res.string.new_password),
                            placeHolder = stringResource(Res.string.please_enter_new_password),
                            value = password?.value ?: "",
                            onValueChange = {
                                interaction?.onUIEvent(ChangePasswordPageEvent.UpdateNewPassword(it))
                            },
                            isInputValid = { isPasswordValid = it },
                            imeAction = ImeAction.Next,
                            focusRequester = passwordFocusRequester,
                            onKeyPressed = { confirmPasswordFocusRequester.requestFocus() },
                            leadingIcon = painterResource(Res.drawable.ic_lock),
                            trailingIcon = painterResource(Res.drawable.ic_eye_off)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ConfirmPasswordField(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            label = stringResource(Res.string.confirm_password),
                            placeHolder = stringResource(Res.string.confirm_new_password),
                            value = confirmPassword?.value ?: "",
                            password = password?.value ?: "",
                            onValueChange = {
                                interaction?.onUIEvent(
                                    ChangePasswordPageEvent.UpdateConfirmPassword(
                                        it
                                    )
                                )
                            },
                            isInputValid = { isConfirmPasswordValid = it },
                            imeAction = ImeAction.Done,
                            focusRequester = confirmPasswordFocusRequester,
                            onKeyPressed = {
                                confirmPasswordFocusRequester.freeFocus()
                                keyboardController?.hide()
                            },
                            leadingIcon = painterResource(Res.drawable.ic_lock),
                            trailingIcon = painterResource(Res.drawable.ic_eye_off)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CustomButton(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            onClick = { interaction?.navigate(ChangePasswordAction.CHANGE) },
                            isEnabled = isPasswordValid && isConfirmPasswordValid && isOldPasswordValid,
                            text = stringResource(Res.string.change_password)
                        )
                    }
                }
            }
    }
}

interface ChangePasswordScreenInteraction: BackPressInteraction {
    fun getOldPassword(): StateFlow<String>?
    fun getPassword(): StateFlow<String>?
    fun getConfirmPassword(): StateFlow<String>?
    fun onUIEvent(event: ChangePasswordPageEvent)
    fun getUiState(): StateFlow<ChangePasswordPageUiState?>
    fun navigate(action: ChangePasswordAction)
}

enum class ChangePasswordAction {
    CHANGE,
    BACK
}


enum class ChangePasswordActionDialogType {
    PASSWORD_CHANGED
}