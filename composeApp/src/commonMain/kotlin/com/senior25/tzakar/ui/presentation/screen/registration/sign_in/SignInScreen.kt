package com.senior25.tzakar.ui.presentation.screen.registration.sign_in

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.data.local.preferences.AppState
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.helper.AppLinks
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.helper.authentication.google.GoogleAuthResponse
import com.senior25.tzakar.helper.encode.encodeUrl
import com.senior25.tzakar.ktx.decodeJson
import com.senior25.tzakar.ktx.encodeToJson
import com.senior25.tzakar.ktx.ifEmpty
import com.senior25.tzakar.ktx.koinParentScreenModel
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.platform_specific.firebase.FirebaseSignOut
import com.senior25.tzakar.platform_specific.getPlatform
import com.senior25.tzakar.platform_specific.toast_helper.showToast
import com.senior25.tzakar.ui.presentation.app.AppNavigator
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.button.GoogleButtonUiContainer
import com.senior25.tzakar.ui.presentation.components.button.OutlinedCustomButton
import com.senior25.tzakar.ui.presentation.components.debounce.debounceClick
import com.senior25.tzakar.ui.presentation.components.debounce.withDebounceAction
import com.senior25.tzakar.ui.presentation.components.fields.EmailField
import com.senior25.tzakar.ui.presentation.components.fields.PasswordField
import com.senior25.tzakar.ui.presentation.components.loader.FullScreenLoader
import com.senior25.tzakar.ui.presentation.dialog.error.ShowErrorDialog
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenLauncher
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreen
import com.senior25.tzakar.ui.presentation.screen.registration.forget_password.ForgotPasswordScreen
import com.senior25.tzakar.ui.presentation.screen.registration.reset_password.ResetPasswordScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.sign_up.SignUpScreen
import com.senior25.tzakar.ui.presentation.screen.web.WebViewScreen
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH1
import com.senior25.tzakar.ui.theme.fontLink
import com.senior25.tzakar.ui.theme.fontParagraphL
import com.senior25.tzakar.ui.theme.fontParagraphM
import com.senior25.tzakar.ui.theme.fontParagraphS
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import io.ktor.util.encodeBase64
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.and_our
import tzakar_reminder.composeapp.generated.resources.app_icon
import tzakar_reminder.composeapp.generated.resources.by_signing_in
import tzakar_reminder.composeapp.generated.resources.close
import tzakar_reminder.composeapp.generated.resources.copyright
import tzakar_reminder.composeapp.generated.resources.dont_have_an_account
import tzakar_reminder.composeapp.generated.resources.email_address
import tzakar_reminder.composeapp.generated.resources.enter_email_address
import tzakar_reminder.composeapp.generated.resources.enter_password
import tzakar_reminder.composeapp.generated.resources.failed
import tzakar_reminder.composeapp.generated.resources.forgot_password
import tzakar_reminder.composeapp.generated.resources.ic_email
import tzakar_reminder.composeapp.generated.resources.ic_eye_off
import tzakar_reminder.composeapp.generated.resources.ic_google
import tzakar_reminder.composeapp.generated.resources.ic_lock
import tzakar_reminder.composeapp.generated.resources.ic_sign_in
import tzakar_reminder.composeapp.generated.resources.lets_sign_in_and_get_starter
import tzakar_reminder.composeapp.generated.resources.password
import tzakar_reminder.composeapp.generated.resources.privacy_policy
import tzakar_reminder.composeapp.generated.resources.sign_in
import tzakar_reminder.composeapp.generated.resources.sign_in_with_google
import tzakar_reminder.composeapp.generated.resources.sign_up
import tzakar_reminder.composeapp.generated.resources.terms_of_service
import tzakar_reminder.composeapp.generated.resources.welcome_back

class SignInScreen:Screen {

    @Composable
    override fun Content() {
        val localNavigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<SignInScreenViewModel>()
        val statusCode = viewModel.errorStatusCode.collectAsState()
        val isLoading = viewModel.isLoading.collectAsState()
        val terms =  stringResource(Res.string.terms_of_service)
        val privacy =  stringResource(Res.string.privacy_policy)

        SignInScreen(interaction = object :SignInScreenInteraction{
            override fun getEmail()  = viewModel.email?:""
            override fun getPassword()  = viewModel.password?:""
            override fun isRememberMe()  = viewModel.isRememberMe == true
            override fun onUIEvent(event: SignInPageEvent) { viewModel.onUIEvent(event) }
            override fun getUiState(): StateFlow<SignInPageUiState?> = viewModel.uiState
            override fun navigate(action: SignInAction) {
                when (action) {
                    SignInAction.APP -> {
                        viewModel.onSignInClick{
                            signOut()
                            SharedPref.appState = AppState.MAIN_ACTIVITY
                            localNavigator.push(MainScreenLauncher())
                        }
                    }

                    SignInAction.GOOGLE -> {
                        SharedPref.isRememberMeChecked = true
                        SharedPref.appState = AppState.MAIN_ACTIVITY
                        localNavigator.push(MainScreenLauncher())
                    }

                    SignInAction.PRIVACY_POLICY -> {
                        localNavigator.push(WebViewScreen(title = privacy, link = AppLinks.PRIVACY.link.encodeUrl()))
                    }

                    SignInAction.TERMS_AND_CONDITION -> {
                        localNavigator.push(WebViewScreen(title =terms,link = AppLinks.TERMS.link.encodeUrl() ))
                    }

                    SignInAction.FORGOT_PASSWORD -> {
                        localNavigator.push(ForgotPasswordScreen())
                    }

                    SignInAction.SIGN_UP -> {
                        localNavigator.push(SignUpScreen())
                    }
                }
            }

            override fun signOut() {
                if (getPlatform().name.contains("Android") )
                    FirebaseSignOut()
                else{
                    viewModel.onSignOut()
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
private fun SignInScreen(interaction: SignInScreenInteraction? = null) {
    val uiState: State<SignInPageUiState?>? = interaction?.getUiState()?.collectAsState()
    var isValidEmail by remember { mutableStateOf(false) }
    var isValidPassword by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    var rememberMe by remember { mutableStateOf(interaction?.isRememberMe()) }

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

        Surface(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
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
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.welcome_back),
                    color = MyColors.colorDarkBlue,
                    textAlign = TextAlign.Center,
                    style = fontH1.copy(fontSize = 34.sp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.lets_sign_in_and_get_starter),
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
                    onValueChange = { interaction?.onUIEvent(SignInPageEvent.UpdateEmail(it)) },
                    isInputValid = { isValidEmail = it },
                    imeAction = ImeAction.Next,
                    focusRequester = emailFocusRequester,
                    onKeyPressed = { passwordFocusRequester.requestFocus() },
                    leadingIcon = painterResource(Res.drawable.ic_email)
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordField (
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    label  = stringResource(Res.string.password),
                    placeHolder = stringResource(Res.string.enter_password),
                    value = interaction?.getPassword(),
                    onValueChange = { interaction?.onUIEvent(SignInPageEvent.UpdatePassword(it)) },
                    isInputValid = { isValidPassword =it },
                    imeAction = ImeAction.Done,
                    focusRequester = passwordFocusRequester,
                    onKeyPressed = {
                        passwordFocusRequester.freeFocus()
                        keyboardController?.hide()
                    },
                    leadingIcon = painterResource(Res.drawable.ic_lock),
                    trailingIcon = painterResource(Res.drawable.ic_eye_off)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
//                    RoundedCheckbox(
//                        checked = rememberMe==true,
//                        onCheckedChange ={
//                            rememberMe = it
//                            interaction?.onUIEvent(SignInPageEvent.UpdateRememberMe(it))
//                        },
//                    ){
//                        Text(
//                            text = stringResource(Res.string.remember_me),
//                            style = fontLink.copy(
//                                color = MyColors.colorDarkBlue,
//                                textAlign = TextAlign.Center,
//                                fontSize = 14.sp
//                            ),
//                        )
//                    }
                    Spacer(modifier = Modifier.width(1.dp))
                    Text(
                        text = stringResource(Res.string.forgot_password),
                        style = fontLink.copy(
                            color = MyColors.colorPurple,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.debounceClick {
                            interaction?.navigate(SignInAction.FORGOT_PASSWORD)
                        },
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                CustomButton(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    onClick =  { interaction?.navigate(SignInAction.APP) },
                    isEnabled = isValidEmail && isValidPassword,
                    endIcon = painterResource(Res.drawable.ic_sign_in),
                    text = stringResource(Res.string.sign_in)
                )
                Spacer(modifier = Modifier.height(16.dp))
                GoogleButtonUiContainer(
                    onResponse = {response->
                        when(response){
                            GoogleAuthResponse.Cancelled ->showToast("cancelled")
                            is GoogleAuthResponse.Error -> showToast(response.message)
                            is GoogleAuthResponse.Success -> {
                                CoroutineScope(Dispatchers.Main).launch{
                                    val email = response.account.profile.email
                                    val ref  = Firebase.database.reference(DataBaseReference.UserProfiles.reference).child(email.encodeBase64())
                                    val userJson  = ref.valueEvents.first().value
                                    val user =  userJson.toString().decodeJson(UserProfile())
                                   val updatedUser =  user?.copy(
                                        email = email,
                                        userName = user.userName?.ifEmpty { null }?:response.account.profile.name
                                    )
                                    ref.setValue(updatedUser.encodeToJson())
                                    SharedPref.loggedInProfile = updatedUser
                                    interaction?.signOut()
                                    interaction?.navigate(SignInAction.GOOGLE)
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                ) {modifier, onclick->
                    OutlinedCustomButton(
                        modifier=modifier,
                        onClick = { onclick.invoke() },
                        keepIconColor = true,
                        startIcon = painterResource(Res.drawable.ic_google),
                        text = stringResource(Res.string.sign_in_with_google)
                    )
                }
                val annotatedText = buildAnnotatedString {
                    append(stringResource(Res.string.dont_have_an_account))
                    append(" ")
                    withDebounceAction(
                        tag = "SIGN_UP",
                        styles = TextLinkStyles(
                            style = SpanStyle( color = MyColors.colorPurple, textDecoration = TextDecoration.Underline),
                            pressedStyle = SpanStyle( color = MyColors.colorPurple, textDecoration = TextDecoration.Underline, background =MyColors.colorLightGrey)
                        ),
                        action = { interaction?.navigate(SignInAction.SIGN_UP) },
                        stringToAppend  = stringResource(Res.string.sign_up)
                    )
                }
                Text(
                    text = annotatedText,
                    style = fontLink.copy(
                        color = MyColors.colorDarkBlue,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val annotatedText = buildAnnotatedString {
                append(stringResource(Res.string.by_signing_in))
                append(" ")
                withDebounceAction(
                    tag = "TERMS_AND_CONDITION",
                    styles = TextLinkStyles(
                        style = SpanStyle( color = MyColors.colorPurple, textDecoration = TextDecoration.Underline),
                        pressedStyle = SpanStyle( color = MyColors.colorPurple, textDecoration = TextDecoration.Underline, background =MyColors.colorLightGrey)
                    ),
                    action = { interaction?.navigate(SignInAction.TERMS_AND_CONDITION) },
                    stringToAppend = stringResource(Res.string.terms_of_service)
                )
                append(" ")
                append(stringResource(Res.string.and_our))
                append(" ")
                withDebounceAction(
                    tag = "PRIVACY_POLICY",
                    styles = TextLinkStyles(
                        style = SpanStyle( color = MyColors.colorPurple, textDecoration = TextDecoration.Underline),
                        pressedStyle = SpanStyle( color = MyColors.colorPurple, textDecoration = TextDecoration.Underline, background =MyColors.colorLightGrey)
                    ),
                    action = { interaction?.navigate(SignInAction.PRIVACY_POLICY) },
                    stringToAppend = stringResource(Res.string.privacy_policy)
                )
            }

            Text(
                text = annotatedText,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = fontParagraphS.copy(
                    textAlign = TextAlign.Center,
                    color = MyColors.colorLightDarkBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.copyright),
                color = MyColors.colorLightDarkBlue,
                textAlign = TextAlign.Center,
                style = fontParagraphM
            )
        }
    }
}

interface SignInScreenInteraction{
    fun getEmail():String
    fun getPassword():String
    fun isRememberMe():Boolean
    fun onUIEvent(event: SignInPageEvent)
    fun getUiState(): StateFlow<SignInPageUiState?>
    fun navigate(action: SignInAction)
    fun signOut()
}

enum class SignInAction {
    APP,
    GOOGLE,
    PRIVACY_POLICY,
    TERMS_AND_CONDITION,
    FORGOT_PASSWORD,
    SIGN_UP
}