package com.senior25.tzakar.ui.presentation.screen.main.profile

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.helper.AppLinks
import com.senior25.tzakar.helper.encode.encodeUrl
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.ui.presentation.app.AppNavigator
import com.senior25.tzakar.ui.presentation.components.separator.Separator
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar
import com.senior25.tzakar.ui.presentation.dialog.delete_account.showDeleteAccountDialog
import com.senior25.tzakar.ui.presentation.dialog.logout.showLogoutDialog
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.change_password.ChangePasswordScreen
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.EditProfileScreen
import com.senior25.tzakar.ui.presentation.screen.web.WebViewScreen
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH3
import com.senior25.tzakar.ui.theme.fontParagraphL
import com.senior25.tzakar.ui.theme.fontParagraphS
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.change_password
import tzakar_reminder.composeapp.generated.resources.delete_account
import tzakar_reminder.composeapp.generated.resources.edit_profile
import tzakar_reminder.composeapp.generated.resources.general
import tzakar_reminder.composeapp.generated.resources.ic_arrow_forward_ios
import tzakar_reminder.composeapp.generated.resources.ic_edit_profile
import tzakar_reminder.composeapp.generated.resources.ic_key
import tzakar_reminder.composeapp.generated.resources.ic_logout
import tzakar_reminder.composeapp.generated.resources.ic_notifications
import tzakar_reminder.composeapp.generated.resources.ic_privacy_policy
import tzakar_reminder.composeapp.generated.resources.ic_terms_service
import tzakar_reminder.composeapp.generated.resources.ic_warning
import tzakar_reminder.composeapp.generated.resources.logout
import tzakar_reminder.composeapp.generated.resources.my_profile
import tzakar_reminder.composeapp.generated.resources.notifications
import tzakar_reminder.composeapp.generated.resources.preferences
import tzakar_reminder.composeapp.generated.resources.privacy_policy
import tzakar_reminder.composeapp.generated.resources.terms_of_service

object ProfileTab: Tab {

    override val key: ScreenKey get() = "ProfileTabKey"

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Person)
            val title  = "My Profile"
            val index:UShort = 1u
            return TabOptions(icon = icon,title = title, index = index)
        }

    @Composable
    override fun Content() {
        Navigator(ProfileScreen(), key = "ProfileTabNavigator"){ navigator->
            SlideTransition(navigator)
        }
    }
}

class ProfileScreen: Screen {

    override val key: ScreenKey get() = "ProfileScreenKey"

    @Composable
    override fun Content() {
        AppNavigator.addTabNavigator(LocalNavigator.current)
        val navigator = LocalNavigator.current
        val mainViewModel = koinScreenModel<MainScreenViewModel>()
        val viewModel = koinScreenModel<ProfileViewModel>()
        val terms =  stringResource(Res.string.terms_of_service)
        val privacy =  stringResource(Res.string.privacy_policy)


        LaunchedEffect(key1 = Unit) { viewModel.init() }

        val interaction  = object : ProfilePageScreenInteraction {

            override fun getUiState(): StateFlow<ProfilePageUiState?> = viewModel.uiState

            override fun getNotificationState(): StateFlow<Boolean?>  = viewModel.notificationState

            override fun onUIEvent(event: ProfilePageEvent) { viewModel.onUIEvent(event) }

            override  fun navigate(action: NavigationAction) {
                when (action) {
                    NavigationAction.EDIT_PROFILE -> navigator?.push(EditProfileScreen())

                    NavigationAction.PRIVACY_POLICY ->{
                        navigator?.push(WebViewScreen(title = privacy, link = AppLinks.PRIVACY.link.encodeUrl()))
                    }

                    NavigationAction.TERMS_AND_CONDITION -> {
                        navigator?.push(WebViewScreen(title =terms,link = AppLinks.TERMS.link.encodeUrl() ))
                    }

                    NavigationAction.LOGOUT -> {
                        onUIEvent(ProfilePageEvent.UpdatePopUpState(ProfilePagePopUp.None))
                        SharedPref.clearPref()
                        AppNavigator.resetNavigation()
                    }

                    NavigationAction.CHANGE_PASSWORD -> {
                        navigator?.push(ChangePasswordScreen())
                    }

                    NavigationAction.DELETE_ACCOUNT -> {
                        viewModel.deleteAccount {
                            onUIEvent(ProfilePageEvent.UpdatePopUpState(ProfilePagePopUp.None))
                            SharedPref.clearPref()
                            AppNavigator.resetNavigation()
                        }
                    }
                }
            }

            override fun onBackPress() { navigator?.pop() }

            override fun getPopupState(): StateFlow<ProfilePagePopUp?> =viewModel.popUpState
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
    val popUpState = interaction?.getPopupState()?.collectAsState()

    val notificationState =  interaction?.getNotificationState()?.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {

            ProfileCard(profile = uiState?.value?.data?.profileModelInfo)

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape =  RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MyColors.colorLightGrey,),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                        text = stringResource(Res.string.general),
                        style = fontH3.copy(color = MyColors.colorLightDarkBlue, textAlign = TextAlign.Center,),
                    )
                    MenuItem(
                        iconRes = Res.drawable.ic_edit_profile,
                        text = stringResource(Res.string.edit_profile)
                    ) {
                        interaction?.navigate(NavigationAction.EDIT_PROFILE)
                    }

                    Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) { Separator() }

                    MenuItem(
                        iconRes = Res.drawable.ic_key,
                        text = stringResource(Res.string.change_password)
                    ) {
                        interaction?.navigate(NavigationAction.CHANGE_PASSWORD)
                    }
                    Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                        Separator()
                    }

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
                Column(modifier = Modifier.fillMaxWidth()) {

                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                        text =stringResource(Res.string.preferences),
                        style = fontH3.copy(
                            color = MyColors.colorLightDarkBlue,
                            textAlign = TextAlign.Center,
                        ),
                    )

                    MenuItemSwitch(
                        iconRes = Res.drawable.ic_notifications,
                        text = stringResource(Res.string.notifications),
                        isSelected = notificationState?.value
                    ) {
                        interaction?.onUIEvent(ProfilePageEvent.UpdateNotificationState(it))
                    }
                    Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                        Separator()
                    }

                    MenuItem(
                        iconRes = Res.drawable.ic_warning,
                        text = stringResource(Res.string.delete_account),
                        textColor = MyColors.colorRed,
                        iconTint = MyColors.colorRed,

                        ) {
                        interaction?.onUIEvent(ProfilePageEvent.UpdatePopUpState(ProfilePagePopUp.DeleteAccount))
                    }
                    Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                        Separator()
                    }
                    MenuItem(
                        iconRes = Res.drawable.ic_logout,
                        text = stringResource(Res.string.logout)
                    ) {
                        interaction?.onUIEvent(ProfilePageEvent.UpdatePopUpState(ProfilePagePopUp.Logout))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "version 1.0.0",
                style = fontParagraphS.copy(color = MyColors.colorLightDarkBlue, textAlign = TextAlign.Start),
            )
        }
    }

    if (popUpState?.value is ProfilePagePopUp.Logout){
        showLogoutDialog(
            onConfirm = { interaction.navigate(NavigationAction.LOGOUT) },
            onDismiss = { interaction?.onUIEvent(ProfilePageEvent.UpdatePopUpState(ProfilePagePopUp.None)) }
        )
    }
    if (popUpState?.value is ProfilePagePopUp.DeleteAccount){
        showDeleteAccountDialog(
            onConfirm = { interaction.navigate(NavigationAction.DELETE_ACCOUNT) },
            onDismiss = { interaction?.onUIEvent(ProfilePageEvent.UpdatePopUpState(ProfilePagePopUp.None)) }
        )
    }
}


@Composable
fun MenuItem(
    iconRes: DrawableResource,
    text: String,
    iconTint:Color = MyColors.colorLightDarkBlue,
    textColor:Color = MyColors.colorLightDarkBlue,
    onClick: () -> Unit,


    ) {
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
            tint = iconTint,
        )
        Text(
            text = text,
            style = fontParagraphL,
            color = textColor,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(Res.drawable.ic_arrow_forward_ios),
            contentDescription = "",
            modifier = Modifier.size(20.dp),
            tint = iconTint
        )
    }
}


@Composable
fun MenuItemSwitch(
    iconRes: DrawableResource,
    text: String,
    iconTint:Color = MyColors.colorLightDarkBlue,
    textColor:Color = MyColors.colorLightDarkBlue,
    isSelected:Boolean? = false,
    onSelect:(Boolean)->Unit = {  },

    ) {
    var isChecked by remember(isSelected) { mutableStateOf(isSelected == true) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
            tint = iconTint,
        )
        Text(
            text = text,
            style = fontParagraphL,
            color = textColor,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .size(52.dp, 32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (isChecked) MyColors.colorDarkBlue else MyColors.colorLightGrey
                )
                .clickable {
                    isChecked = !isChecked
                    onSelect(isChecked)
                }
                .padding(horizontal = 4.dp)

        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(if (isChecked) Alignment.CenterEnd else Alignment.CenterStart) // Thumb position
                    .clip(CircleShape)
                    .background(MyColors.colorPurple)
            )
        }

    }
}

interface ProfilePageScreenInteraction{
    fun getUiState(): StateFlow<ProfilePageUiState?>
    fun getNotificationState(): StateFlow<Boolean?>
    fun onUIEvent(event: ProfilePageEvent)
    fun navigate(action: NavigationAction)
    fun onBackPress()
    fun getPopupState(): StateFlow<ProfilePagePopUp?>
}

enum class NavigationAction {
    EDIT_PROFILE,
    CHANGE_PASSWORD,
    PRIVACY_POLICY,
    DELETE_ACCOUNT,
    TERMS_AND_CONDITION,
    LOGOUT
}