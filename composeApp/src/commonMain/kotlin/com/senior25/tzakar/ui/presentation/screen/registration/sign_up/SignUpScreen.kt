package com.senior25.tzakar.ui.presentation.screen.registration.sign_up

//import cafe.adriel.voyager.koin.koinScreenModel
//import com.senior25.tzakar.ktx.getNavigatorScreenModel
//import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreenViewModel
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.helper.AppLinks
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.helper.authentication.google.GoogleAuthResponse
import com.senior25.tzakar.helper.encode.encodeUrl
import com.senior25.tzakar.ktx.decodeJson
import com.senior25.tzakar.ktx.encodeToJson
import com.senior25.tzakar.ktx.koinParentScreenModel
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.platform_specific.toast_helper.showToast
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.button.GoogleButtonUiContainer
import com.senior25.tzakar.ui.presentation.components.button.OutlinedCustomButton
import com.senior25.tzakar.ui.presentation.components.debounce.withDebounceAction
import com.senior25.tzakar.ui.presentation.components.fields.EmailField
import com.senior25.tzakar.ui.presentation.components.fields.PasswordField
import com.senior25.tzakar.ui.presentation.components.fields.userNameField
import com.senior25.tzakar.ui.presentation.components.loader.FullScreenLoader
import com.senior25.tzakar.ui.presentation.dialog.ShowDialog
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenLauncher
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreen
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.web.WebViewScreen
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH1
import com.senior25.tzakar.ui.theme.fontLink
import com.senior25.tzakar.ui.theme.fontParagraphL
import com.senior25.tzakar.ui.theme.fontParagraphM
import com.senior25.tzakar.ui.theme.fontParagraphS
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
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
import tzakar_reminder.composeapp.generated.resources.already_have_an_account
import tzakar_reminder.composeapp.generated.resources.and_our
import tzakar_reminder.composeapp.generated.resources.app_icon
import tzakar_reminder.composeapp.generated.resources.by_signing_in
import tzakar_reminder.composeapp.generated.resources.close
import tzakar_reminder.composeapp.generated.resources.copyright
import tzakar_reminder.composeapp.generated.resources.email_address
import tzakar_reminder.composeapp.generated.resources.enter_email_address
import tzakar_reminder.composeapp.generated.resources.enter_password
import tzakar_reminder.composeapp.generated.resources.enter_username
import tzakar_reminder.composeapp.generated.resources.failed
import tzakar_reminder.composeapp.generated.resources.ic_email
import tzakar_reminder.composeapp.generated.resources.ic_eye_off
import tzakar_reminder.composeapp.generated.resources.ic_google
import tzakar_reminder.composeapp.generated.resources.ic_lock
import tzakar_reminder.composeapp.generated.resources.ic_person
import tzakar_reminder.composeapp.generated.resources.ic_sign_in
import tzakar_reminder.composeapp.generated.resources.lets_create_your_account
import tzakar_reminder.composeapp.generated.resources.lets_sign_up_and_join_the_journey
import tzakar_reminder.composeapp.generated.resources.password
import tzakar_reminder.composeapp.generated.resources.privacy_policy
import tzakar_reminder.composeapp.generated.resources.sign_in
import tzakar_reminder.composeapp.generated.resources.sign_up
import tzakar_reminder.composeapp.generated.resources.sign_up_with_google
import tzakar_reminder.composeapp.generated.resources.terms_of_service
import tzakar_reminder.composeapp.generated.resources.username


data class SignUpScreen(val sharedViewModel: RegistrationScreenViewModel? = null):Screen {
    @Composable
    override fun Content() {
        val localNavigator = LocalNavigator.currentOrThrow
        val sharedViewModel = localNavigator?.koinParentScreenModel<RegistrationScreenViewModel>(
            parentName = RegistrationScreen::class.simpleName
        )?:koinScreenModel()
        val viewModel = koinScreenModel<SignUpScreenViewModel>()
        val statusCode = viewModel.errorStatusCode.collectAsState()
        val isLoading = viewModel.isLoading.collectAsState()
        val terms =  stringResource(Res.string.terms_of_service)
        val privacy =  stringResource(Res.string.privacy_policy)

        SignUpScreen(interaction = object : SignUpScreenInteraction {
            override fun getEmail() = viewModel.email?:"ramsiskhortoum1@gmail.com"
            override fun getUsername() = viewModel.username?:"ramsis"
            override fun getPassword() = viewModel.password?:"Test@123"
            override fun onUIEvent(event: SignUpPageEvent) { viewModel.onUIEvent(event) }
            override fun getUiState(): StateFlow<SignUpPageUiState?> = viewModel.uiState
            override fun navigate(action: SignUpAction) {
                when (action) {
                    SignUpAction.SIGN_UP -> {
                        viewModel.createUser{
                            SharedPref.loggedInEmail = viewModel.email
                            signOut()
                            localNavigator.push(MainScreenLauncher())
                        }
                    }
                    SignUpAction.SIGN_IN -> {
                        localNavigator.pop()
                    }
                    SignUpAction.GOOGLE -> {
                        localNavigator.push(MainScreenLauncher())
                    }

                    SignUpAction.PRIVACY_POLICY -> {
                        localNavigator.push(WebViewScreen(title = privacy, link = AppLinks.PRIVACY.link.encodeUrl()))
                    }

                    SignUpAction.TERMS_AND_CONDITION -> {
                        localNavigator.push(WebViewScreen(title =terms,link = AppLinks.PRIVACY.link.encodeUrl() ))
                    }
                }
            }
            override fun signOut() {
                viewModel.onSignOut()
            }

        })

        statusCode.value?.let {
            ShowDialog(
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
private fun SignUpScreen(interaction: SignUpScreenInteraction? = null) {
    val uiState: State<SignUpPageUiState?>? = interaction?.getUiState()?.collectAsState()
    var isValidEmail by remember { mutableStateOf(false) }
    var isValidUsername by remember { mutableStateOf(false) }
    var isValidPassword by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val emailFocusRequester = remember { FocusRequester() }
    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    Column(
        modifier =  Modifier.fillMaxSize()
            .background(MyColors.colorOffWhite)
            .verticalScroll(rememberScrollState())
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
                style = fontH1.copy(
                    color = MyColors.colorDarkBlue,
                    textAlign = TextAlign.Center,
                ),
            )
        }

        Surface(
            modifier = Modifier
                .padding(vertical = 16.dp)
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
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.lets_create_your_account),
                    color = MyColors.colorDarkBlue,
                    textAlign = TextAlign.Center,
                    style = fontH1.copy(fontSize = 34.sp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.lets_sign_up_and_join_the_journey),
                    color = MyColors.colorLightDarkBlue,
                    textAlign = TextAlign.Center,
                    style = fontParagraphL
                )

                Spacer(modifier = Modifier.height(24.dp))

                userNameField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    label = stringResource(Res.string.username),
                    placeHolder = stringResource(Res.string.enter_username),
                    value = interaction?.getUsername(),
                    onValueChange = { interaction?.onUIEvent(SignUpPageEvent.UpdateUsername(it)) },
                    isInputValid = { isValidUsername = it },
                    imeAction = ImeAction.Next,
                    focusRequester = usernameFocusRequester,
                    onKeyPressed = { emailFocusRequester.requestFocus() },
                    leadingIcon = painterResource(Res.drawable.ic_person)
                )

                Spacer(modifier = Modifier.height(16.dp))

                EmailField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    label = stringResource(Res.string.email_address),
                    placeHolder = stringResource(Res.string.enter_email_address),
                    value = interaction?.getEmail(),
                    onValueChange = { interaction?.onUIEvent(SignUpPageEvent.UpdateEmail(it)) },
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
                    onValueChange = { interaction?.onUIEvent(SignUpPageEvent.UpdatePassword(it)) },
                    isInputValid = { isValidPassword = it },
                    imeAction = ImeAction.Done,
                    focusRequester = passwordFocusRequester,
                    onKeyPressed = { passwordFocusRequester.freeFocus();keyboardController?.hide() },
                    leadingIcon = painterResource(Res.drawable.ic_lock),
                    trailingIcon = painterResource(Res.drawable.ic_eye_off)
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomButton(
                    isEnabled = isValidEmail && isValidPassword && isValidUsername,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    onClick = { interaction?.navigate(SignUpAction.SIGN_UP) },
                    endIcon = painterResource(Res.drawable.ic_sign_in),
                    text = stringResource(Res.string.sign_up)
                )
                Spacer(modifier = Modifier.height(16.dp))

                GoogleButtonUiContainer(
                    onResponse = { response->
                        when(response){
                            GoogleAuthResponse.Cancelled ->showToast("cancelled")
                            is GoogleAuthResponse.Error -> showToast(response.message)
                            is GoogleAuthResponse.Success -> {
                                CoroutineScope(Dispatchers.Main).launch{
                                    val email = response.account.profile.email
                                    val ref  = Firebase.database.reference(DataBaseReference.UserProfiles.reference).child(email.encodeBase64())
                                    val userJson  = ref.valueEvents.first().value
                                    val user =  userJson.toString().decodeJson(UserProfile())
                                    ref.setValue(user?.copy(email = email).encodeToJson())
                                    SharedPref.loggedInEmail = email
                                    Firebase.auth.signOut()
                                    interaction?.navigate(SignUpAction.GOOGLE)
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
                        text = stringResource(Res.string.sign_up_with_google)
                    )
                }

                val annotatedText = buildAnnotatedString {
                    append(stringResource(Res.string.already_have_an_account))
                    append(" ")
                    withDebounceAction(
                        tag = "SIGN_IN",
                        styles = TextLinkStyles(
                            style = SpanStyle(
                                color = MyColors.colorPurple,
                                textDecoration = TextDecoration.Underline
                            ),
                            pressedStyle = SpanStyle(
                                color = MyColors.colorPurple,
                                textDecoration = TextDecoration.Underline,
                                background =MyColors.colorLightGrey
                            )
                        ),
                        action = { interaction?.navigate(SignUpAction.SIGN_IN) },
                        stringToAppend = stringResource(Res.string.sign_in)
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
                    action = { interaction?.navigate(SignUpAction.TERMS_AND_CONDITION) },
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
                    action = { interaction?.navigate(SignUpAction.PRIVACY_POLICY) },
                    stringToAppend =  stringResource(Res.string.privacy_policy)
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

interface SignUpScreenInteraction{
    fun getEmail():String
    fun getUsername():String
    fun getPassword():String
    fun onUIEvent(event: SignUpPageEvent)
    fun getUiState(): StateFlow<SignUpPageUiState?>
    fun navigate(action: SignUpAction)
    fun signOut()

}

enum class SignUpAction {
    PRIVACY_POLICY,
    TERMS_AND_CONDITION,
    SIGN_UP,
    GOOGLE,
    SIGN_IN
}