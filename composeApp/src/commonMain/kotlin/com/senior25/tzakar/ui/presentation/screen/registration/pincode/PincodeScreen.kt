package com.senior25.tzakar.ui.presentation.screen.registration.pincode

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.ktx.koinParentScreenModel
//import com.senior25.tzakar.ktx.getScreenModel
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.debounce.debounceClick
import com.senior25.tzakar.ui.presentation.components.debounce.rememberDebounceClick
import com.senior25.tzakar.ui.presentation.components.debounce.withDebounceAction
import com.senior25.tzakar.ui.presentation.components.fields.EmailField
import com.senior25.tzakar.ui.presentation.components.fields.OtpTextField
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreen
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreen
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.reset_password.ResetPasswordScreen
import com.senior25.tzakar.ui.presentation.screen.registration.sign_in.SignInAction
import com.senior25.tzakar.ui.presentation.screen.registration.sign_up.SignUpAction
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH1
import com.senior25.tzakar.ui.theme.fontLink
import com.senior25.tzakar.ui.theme.fontParagraphL
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.app_icon
import tzakar_reminder.composeapp.generated.resources.back
import tzakar_reminder.composeapp.generated.resources.back_to_login_screen
import tzakar_reminder.composeapp.generated.resources.code_verification
import tzakar_reminder.composeapp.generated.resources.dont_have_an_account
import tzakar_reminder.composeapp.generated.resources.dont_worry_we_can_restore_it
import tzakar_reminder.composeapp.generated.resources.email_address
import tzakar_reminder.composeapp.generated.resources.enter_email_address
import tzakar_reminder.composeapp.generated.resources.ic_back
import tzakar_reminder.composeapp.generated.resources.ic_email
import tzakar_reminder.composeapp.generated.resources.reset_password
import tzakar_reminder.composeapp.generated.resources.reset_your_password
import tzakar_reminder.composeapp.generated.resources.sign_up
import tzakar_reminder.composeapp.generated.resources.verify
import tzakar_reminder.composeapp.generated.resources.we_have_sent_a_code_to

class PinCodeScreen:Screen {
    @Composable
    override fun Content() {
        val localNavigator = LocalNavigator.currentOrThrow

        val viewModel = koinScreenModel<PinCodeScreenViewModel>()

        val sharedViewModel = localNavigator?.koinParentScreenModel<RegistrationScreenViewModel>(
            parentName = RegistrationScreen::class.simpleName
        )?:koinScreenModel()

        PinCodeScreen(interaction = object : PinCodeScreenInteraction {

            override fun getEmail(): String = sharedViewModel.registrationData.email?:""

            override fun onUIEvent(event: PinCodeScreenPageEvent) { viewModel.onUIEvent(event) }

            override fun getUiState(): StateFlow<PinCodeScreenPageUiState?> = viewModel.uiState

            override fun navigate(action: PinCodeScreenAction) {
                when (action) {
                    PinCodeScreenAction.VALIDATE ->localNavigator.push(ResetPasswordScreen())
                    PinCodeScreenAction.GO_BACK -> localNavigator.pop()
                }
            }

            override fun getPinCodeMap(): String?  =  viewModel.pinCodeMap
        })
    }
}

@Composable
private fun PinCodeScreen(interaction: PinCodeScreenInteraction? = null) {
    var isValidPinCode by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

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
                        text = stringResource(Res.string.code_verification),
                        color = MyColors.colorDarkBlue,
                        textAlign = TextAlign.Center,
                        style = fontH1.copy(fontSize = 34.sp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    val annotatedText = buildAnnotatedString {
                        append(stringResource(Res.string.we_have_sent_a_code_to))
                        append(":\n")
                        withDebounceAction(
                            tag = "",
                            styles = TextLinkStyles(style =SpanStyle( color = MyColors.colorPurple),),
                            action = {  },
                            stringToAppend  = interaction?.getEmail()?:""
                        )
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = annotatedText,
                        color = MyColors.colorLightDarkBlue,
                        textAlign = TextAlign.Center,
                        style = fontParagraphL
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    var otpValue by remember { mutableStateOf("") }
                    CompositionLocalProvider(
                        LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
                        LocalLayoutDirection provides LayoutDirection.Ltr
                    ) {
                        OtpTextField(
                            modifier = Modifier.fillMaxWidth(),
                            otpText = otpValue,
                            onOtpTextChange = { value, otpInputFilled ->
                                otpValue = value
                                isValidPinCode = (otpValue.length ==6)
                                if (isValidPinCode) keyboardController?.hide()
                                interaction?.onUIEvent(PinCodeScreenPageEvent.UpdatePinCodeMap(otpValue))
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    CustomButton(
                        isEnabled = isValidPinCode ,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        onClick = {
                            interaction?.navigate(PinCodeScreenAction.VALIDATE)
                        },
                        text = stringResource(Res.string.verify)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().debounceClick {
                    interaction?.navigate(PinCodeScreenAction.GO_BACK)
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

interface PinCodeScreenInteraction{
    fun getEmail():String
    fun onUIEvent(event: PinCodeScreenPageEvent)
    fun getUiState(): StateFlow<PinCodeScreenPageUiState?>
    fun navigate(action: PinCodeScreenAction)
    fun getPinCodeMap():String?
}

enum class PinCodeScreenAction {
    VALIDATE,
    GO_BACK
}