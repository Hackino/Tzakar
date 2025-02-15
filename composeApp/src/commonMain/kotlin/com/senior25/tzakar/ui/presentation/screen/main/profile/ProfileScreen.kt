package com.senior25.tzakar.ui.presentation.screen.main.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.helper.AppLinks
import com.senior25.tzakar.helper.encode.encodeUrl
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.ui.presentation.components.debounce.rememberDebounceClick
import com.senior25.tzakar.ui.presentation.components.separator.Separator
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar
import com.senior25.tzakar.ui.presentation.dialog.edit_profile.ShowSaveProfileConfirmation
import com.senior25.tzakar.ui.presentation.dialog.logout.showLogoutDialog
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.EditProfilePageEvent
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.EditProfilePagePopUp
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.EditProfileScreen
import com.senior25.tzakar.ui.presentation.screen.web.WebViewScreen
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH1
import com.senior25.tzakar.ui.theme.fontParagraphL
import com.senior25.tzakar.ui.theme.fontParagraphS
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.app_icon
import tzakar_reminder.composeapp.generated.resources.ic_arrow_forward_ios
import tzakar_reminder.composeapp.generated.resources.ic_logout
import tzakar_reminder.composeapp.generated.resources.ic_privacy_policy
import tzakar_reminder.composeapp.generated.resources.ic_terms_service
import tzakar_reminder.composeapp.generated.resources.logout
import tzakar_reminder.composeapp.generated.resources.my_profile
import tzakar_reminder.composeapp.generated.resources.privacy_policy
import tzakar_reminder.composeapp.generated.resources.terms_of_service

object ProfileTab: Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Person)
            val title  = "My Profile"
            val index:UShort= 1u
            return TabOptions(icon = icon,title = title, index = index)
        }

    @Composable
    override fun Content() {
        Navigator(ProfileScreen()){navigator->
            SlideTransition(navigator)
        }
    }
}

class ProfileScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val screenModel = koinScreenModel<ProfileViewModel>()
        val terms =  stringResource(Res.string.terms_of_service)
        val privacy =  stringResource(Res.string.privacy_policy)

//        LaunchedEffect(Unit) { screenModel.init() }

        val interaction  = object : ProfilePageScreenInteraction {

            override fun getUiState(): StateFlow<ProfilePageUiState?> = screenModel.uiState

            override fun onUIEvent(event: ProfilePageEvent) {
                screenModel.onUIEvent(event)
            }

            override  fun navigate(action: NavigationAction) {
                when (action) {
                    NavigationAction.EDIT_PROFILE -> {
                        navigator?.push(EditProfileScreen())
                    }

                    NavigationAction.PRIVACY_POLICY ->{
                        navigator?.push(WebViewScreen(title = privacy, link = AppLinks.PRIVACY.link.encodeUrl()))
                    }
                    NavigationAction.TERMS_AND_CONDITION -> {
                        navigator?.push(WebViewScreen(title =terms,link = AppLinks.TERMS.link.encodeUrl() ))

                    }
                    NavigationAction.LOGOUT -> {
                        onUIEvent(ProfilePageEvent.UpdatePopUpState(ProfilePagePopUp.None))
                    }
                }
            }

            override fun onBackPress() {
                navigator?.pop()
            }

            override fun getPopupState(): StateFlow<ProfilePagePopUp?> =screenModel.popUpState
        }
        Scaffold(
            backgroundColor = MyColors.colorOffWhite,
            topBar = { MyTopAppBar( stringResource(Res.string.my_profile), showBack = false) },
            content = { ProfileScreen(interaction) }
        )
    }
}


@Composable
fun ProfileScreen(interaction: ProfilePageScreenInteraction?) {
    val uiState =  interaction?.getUiState()?.collectAsState()
    val debouncedOnClick = rememberDebounceClick {  interaction?.onBackPress()  }
    val popUpState = interaction?.getPopupState()?.collectAsState()


    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            HeaderSection()

            ProfileCard(
                profile = uiState?.value?.data?.profileModelInfo,
                onEditClick = { interaction?.navigate(NavigationAction.EDIT_PROFILE) },
            )

            Spacer(modifier = Modifier.height(16.dp))

//                    MenuItem(
//                        iconRes = Res.drawable.ic_calendar_checked,
//                        text = stringResource(Res.string.my_appointments)) {
//                        interaction?.navigate(NavigationAction.MY_APPOINTMENTS)
//                    }
//                    Separator()


            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape =  RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MyColors.colorLightGrey,),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    MenuItem(
                        iconRes = Res.drawable.ic_terms_service,
                        text = stringResource(Res.string.terms_of_service)
                    ) {
                        interaction?.navigate(NavigationAction.TERMS_AND_CONDITION)
                    }
                    Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                        Separator()
                    }

                    MenuItem(
                        iconRes = Res.drawable.ic_privacy_policy,
                        text = stringResource(Res.string.privacy_policy)
                    ) {
                        interaction?.navigate(NavigationAction.PRIVACY_POLICY)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape =  RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MyColors.colorLightGrey,),
                elevation = 4.dp
            ) {
                MenuItem(
                    iconRes = Res.drawable.ic_logout,
                    text = stringResource(Res.string.logout)
                ) {
                    interaction?.onUIEvent(ProfilePageEvent.UpdatePopUpState(ProfilePagePopUp.Logout))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "version 1.0.0",
                style = fontParagraphS.copy(color = MyColors.colorLightDarkBlue, textAlign = TextAlign.Start),
            )
        }

//        if (uiState?.value is ProfilePageUiState.Loading) FullScreenLoader()

    }

    if (popUpState?.value is ProfilePagePopUp.Logout){
        showLogoutDialog(
            onConfirm = { interaction.navigate(NavigationAction.LOGOUT) },
            onDismiss = { interaction?.onUIEvent(ProfilePageEvent.UpdatePopUpState(ProfilePagePopUp.None)) }
        )
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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

    }
}

@Composable
fun MenuItem(iconRes: DrawableResource, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
            .clip(RectangleShape)
            .then(Modifier.padding(vertical = 8.dp))
            .padding(bottom = 8.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = text,
            modifier = Modifier.size(24.dp),
            tint = MyColors.colorLightDarkBlue,
        )
        Text(
            text = text,
            style = fontParagraphL,
            color = MyColors.colorLightDarkBlue,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(Res.drawable.ic_arrow_forward_ios),
            contentDescription = "",
            modifier = Modifier.size(20.dp),
            tint = MyColors.colorLightDarkBlue
        )
    }
}


interface ProfilePageScreenInteraction{
    fun getUiState(): StateFlow<ProfilePageUiState?>
    fun onUIEvent(event: ProfilePageEvent)
    fun navigate(action: NavigationAction)
    fun onBackPress()
    fun getPopupState(): StateFlow<ProfilePagePopUp?>

}

enum class NavigationAction {
    EDIT_PROFILE,
    PRIVACY_POLICY,
    TERMS_AND_CONDITION,
    LOGOUT
}