package com.senior25.tzakar.ui.presentation.screen.registration.reset_password

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
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
import com.senior25.tzakar.ktx.koinParentScreenModel
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.debounce.debounceClick
import com.senior25.tzakar.ui.presentation.components.fields.ConfirmPasswordField
import com.senior25.tzakar.ui.presentation.components.fields.PasswordField
import com.senior25.tzakar.ui.presentation.components.loader.FullScreenLoader
import com.senior25.tzakar.ui.presentation.dialog.error.ShowErrorDialog
import com.senior25.tzakar.ui.presentation.dialog.pasword_reset.showPasswordResetDialog
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreen
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.forget_password.ForgotPasswordScreen
import com.senior25.tzakar.ui.presentation.screen.registration.sign_in.SignInScreen
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
import tzakar_reminder.composeapp.generated.resources.close
import tzakar_reminder.composeapp.generated.resources.confirm_password
import tzakar_reminder.composeapp.generated.resources.dont_worry_resetting_your_passowrd_is_easy
import tzakar_reminder.composeapp.generated.resources.enter_password
import tzakar_reminder.composeapp.generated.resources.failed
import tzakar_reminder.composeapp.generated.resources.ic_back
import tzakar_reminder.composeapp.generated.resources.ic_eye_off
import tzakar_reminder.composeapp.generated.resources.ic_lock
import tzakar_reminder.composeapp.generated.resources.password
import tzakar_reminder.composeapp.generated.resources.reset_password

class ResetPasswordScreen:Screen {

    @Composable
    override fun Content() {
        val localNavigator = LocalNavigator.currentOrThrow
        val sharedViewModel = localNavigator?.koinParentScreenModel<RegistrationScreenViewModel>(
            parentName = RegistrationScreen::class.simpleName
        )?:koinScreenModel()

        val viewModel = koinScreenModel<ResetPasswordScreenViewModel>()
        val statusCode = viewModel.errorStatusCode.collectAsState()
        val isLoading = viewModel.isLoading.collectAsState()
        val dialogType = viewModel.dialogType.collectAsState()


        SignInScreen(interaction = object :ResetPasswordScreenInteraction{
            override fun getPassword():  StateFlow<String> = viewModel.password
            override fun getConfirmPassword():  StateFlow<String> = viewModel.confirmPassword
            override fun onUIEvent(event: ResetPasswordPageEvent) { viewModel.onUIEvent(event) }
            override fun getUiState(): StateFlow<ResetPasswordPageUiState?> = viewModel.uiState
            override fun navigate(action: ResetPasswordAction) {
                when (action) {
                    ResetPasswordAction.RESET ->{
                        viewModel.resetPassword(sharedViewModel.registrationData.email){
                            viewModel._dialogType.value = ResetPasswordActionDialogType.PASSWORD_RESET
                        }
                    }
                    ResetPasswordAction.BACK -> localNavigator.popUntil { it::class.simpleName == ForgotPasswordScreen::class.simpleName }
                }
            }
        })

        statusCode.value?.let {
            ShowErrorDialog(
                title = stringResource(Res.string.failed),
                message = it.errorMessage.toString(),
                onConfirm = { viewModel._errorStatusCode.value = null },
                confirmText = stringResource(Res.string.close)
            )
        }

        when(dialogType.value){
            ResetPasswordActionDialogType.PASSWORD_RESET -> {
                showPasswordResetDialog{
                    localNavigator.popUntil { it::class.simpleName == SignInScreen::class.simpleName }
                    viewModel._dialogType.value = null
                }
            }
            null ->Unit
        }

        isLoading.value?.let { FullScreenLoader() }
    }
}

@Composable
private fun SignInScreen(interaction: ResetPasswordScreenInteraction? = null) {
    var isPasswordValid by remember { mutableStateOf(false) }
    var isConfirmPasswordValid by remember { mutableStateOf(false) }
    val uiState: State<ResetPasswordPageUiState?>? = interaction?.getUiState()?.collectAsState()

    val password: State<String>? = interaction?.getPassword()?.collectAsState()
    val confirmPassword: State<String>? = interaction?.getConfirmPassword()?.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    Column(
        modifier =  Modifier.fillMaxSize()
            .background(MyColors.colorOffWhite)
            .padding(bottom = 24.dp, top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                modifier = Modifier.size(48.dp),
                painter = painterResource(Res.drawable.app_icon),
                contentDescription =  ""
            )
            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MyColors.colorPurple)) { append("T") }
                append("zakar")
            }
            Text(
                text = annotatedText,
                style = fontH1.copy(color = MyColors.colorDarkBlue, textAlign = TextAlign.Center,),
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MyColors.colorLightGrey,),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.reset_password),
                        color = MyColors.colorDarkBlue,
                        textAlign = TextAlign.Center,
                        style = fontH1.copy(fontSize = 34.sp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.dont_worry_resetting_your_passowrd_is_easy),
                        color = MyColors.colorLightDarkBlue,
                        textAlign = TextAlign.Center,
                        style = fontParagraphL
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    PasswordField(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        label = stringResource(Res.string.password),
                        placeHolder = stringResource(Res.string.enter_password),
                        value = password?.value ?: "",
                        onValueChange = {
                            interaction?.onUIEvent(ResetPasswordPageEvent.UpdatePassword(it))
                        },
                        isInputValid = { isPasswordValid = it },
                        imeAction = ImeAction.Next,
                        focusRequester = passwordFocusRequester,
                        onKeyPressed = { passwordFocusRequester.requestFocus() },
                        leadingIcon = painterResource(Res.drawable.ic_lock),
                        trailingIcon = painterResource(Res.drawable.ic_eye_off)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ConfirmPasswordField(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        label = stringResource(Res.string.confirm_password),
                        placeHolder = stringResource(Res.string.enter_password),
                        value = confirmPassword?.value ?: "",
                        password = password?.value?:"",
                        onValueChange = {
                            interaction?.onUIEvent(ResetPasswordPageEvent.UpdateConfirmPassword(it))
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

                    CustomButton(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        onClick = { interaction?.navigate(ResetPasswordAction.RESET) },
                        isEnabled = isPasswordValid && isConfirmPasswordValid,
                        text = stringResource(Res.string.reset_password)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().debounceClick {
                    interaction?.navigate(ResetPasswordAction.BACK)
                },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MyColors.colorPurple
                )
                Text(
                    modifier = Modifier,
                    text = stringResource(Res.string.back),
                    style = fontLink.copy(
                        color = MyColors.colorPurple,
                        textAlign = TextAlign.Center,
                    ),
                )
            }
        }
        Row {/*do not remove*/  }
    }
}

interface ResetPasswordScreenInteraction{
    fun getPassword(): StateFlow<String>?
    fun getConfirmPassword(): StateFlow<String>?
    fun onUIEvent(event: ResetPasswordPageEvent)
    fun getUiState(): StateFlow<ResetPasswordPageUiState?>
    fun navigate(action: ResetPasswordAction)
}

enum class ResetPasswordAction {
    RESET,
    BACK
}


enum class ResetPasswordActionDialogType {
    PASSWORD_RESET
}