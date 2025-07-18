package com.senior25.tzakar.ui.presentation.screen.registration.forget_password

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import com.senior25.tzakar.ui.presentation.components.fields.EmailField
import com.senior25.tzakar.ui.presentation.components.loader.FullScreenLoader
import com.senior25.tzakar.ui.presentation.dialog.error.ShowErrorDialog
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreen
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.pincode.PinCodeScreen
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH1
import com.senior25.tzakar.ui.theme.fontLink
import com.senior25.tzakar.ui.theme.fontParagraphL
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.app_icon
import tzakar_reminder.composeapp.generated.resources.back_to_login_screen
import tzakar_reminder.composeapp.generated.resources.close
import tzakar_reminder.composeapp.generated.resources.dont_worry_we_can_restore_it
import tzakar_reminder.composeapp.generated.resources.email_address
import tzakar_reminder.composeapp.generated.resources.enter_email_address
import tzakar_reminder.composeapp.generated.resources.failed
import tzakar_reminder.composeapp.generated.resources.forgot_password
import tzakar_reminder.composeapp.generated.resources.ic_back
import tzakar_reminder.composeapp.generated.resources.ic_email
import tzakar_reminder.composeapp.generated.resources.send_code


class ForgotPasswordScreen:Screen {

    @Composable
    override fun Content() {
        val localNavigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<ForgotPasswordScreenViewModel>()
        val sharedViewModel = localNavigator?.koinParentScreenModel<RegistrationScreenViewModel>(
            parentName = RegistrationScreen::class.simpleName
        )?:koinScreenModel()
        val statusCode = viewModel.errorStatusCode.collectAsState()
        val isLoading = viewModel.isLoading.collectAsState()

        ForgotPasswordScreen(interaction = object : ForgotPasswordScreenInteraction {
            override fun getEmail() = viewModel.email ?: ""

            override fun onUIEvent(event: ForgotPasswordPageEvent) {
                viewModel.onUIEvent(event)
            }

            override fun getUiState(): StateFlow<ForgotPasswordPageUiState?> = viewModel.uiState
            override fun navigate(action: ForgotPasswordAction) {
                when (action) {
                    ForgotPasswordAction.GO_BACK_TO_LOGIN -> { localNavigator.pop() }
                    ForgotPasswordAction.RESET -> {
                        viewModel.checkEmail(viewModel.email){
                            sharedViewModel.registrationData = sharedViewModel.registrationData.copy(
                                email = viewModel.email,
                            )
                            localNavigator.push(PinCodeScreen())
                        }
                    }
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
        isLoading.value?.let { FullScreenLoader() }
    }
}

@Composable
private fun ForgotPasswordScreen(interaction: ForgotPasswordScreenInteraction? = null) {
    var isValidEmail by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val emailFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier.fillMaxSize()
            .navigationBarsPadding()
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
                withStyle(style = SpanStyle(color = MyColors.colorPurple)) {
                    append("T")
                }
                append("zakar")
            }
            Text(
                text = annotatedText,
                style = fontH1.copy(
                    color = MyColors.colorDarkBlue,
                    textAlign = TextAlign.Center,
                ),
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape =  RoundedCornerShape(16.dp),
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
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        text = stringResource(Res.string.forgot_password),
                        color = MyColors.colorDarkBlue,
                        textAlign = TextAlign.Center,
                        style = fontH1.copy(fontSize = 34.sp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.dont_worry_we_can_restore_it),
                        color = MyColors.colorLightDarkBlue,
                        textAlign = TextAlign.Center,
                        style = fontParagraphL
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    EmailField(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        label = stringResource(Res.string.email_address),
                        placeHolder = stringResource(Res.string.enter_email_address),
                        value = interaction?.getEmail(),
                        onValueChange = { interaction?.onUIEvent(ForgotPasswordPageEvent.UpdateEmail(it)) },
                        isInputValid = { isValidEmail = it },
                        imeAction = ImeAction.Done,
                        focusRequester = emailFocusRequester,
                        onKeyPressed = {
                            emailFocusRequester.freeFocus()
                            keyboardController?.hide()
                        },
                        leadingIcon = painterResource(Res.drawable.ic_email)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    CustomButton(
                        isEnabled = isValidEmail ,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        onClick = {
                            interaction?.navigate(ForgotPasswordAction.RESET)
                        },
                        text = stringResource(Res.string.send_code)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().debounceClick {
                    interaction?.navigate(ForgotPasswordAction.GO_BACK_TO_LOGIN)
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
                    text = stringResource(Res.string.back_to_login_screen),
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

interface ForgotPasswordScreenInteraction{
    fun getEmail():String
    fun onUIEvent(event: ForgotPasswordPageEvent)
    fun getUiState(): StateFlow<ForgotPasswordPageUiState?>
    fun navigate(action: ForgotPasswordAction)
}

enum class ForgotPasswordAction {
    GO_BACK_TO_LOGIN,
    RESET

}