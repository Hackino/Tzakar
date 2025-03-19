package com.senior25.tzakar.ui.presentation.screen.main.edit_profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.data.local.model.avatars.AvatarsModel
import com.senior25.tzakar.data.local.model.gender.Gender
import com.senior25.tzakar.data.local.model.gender.GenderModel
import com.senior25.tzakar.data.local.model.gender.Genders
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.platform_specific.ui.getScreenHeight
import com.senior25.tzakar.platform_specific.ui.getScreenWidth
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.fields.DropDownField
import com.senior25.tzakar.ui.presentation.components.fields.EmailField
import com.senior25.tzakar.ui.presentation.components.fields.calculateDropdownOffset
import com.senior25.tzakar.ui.presentation.components.fields.userNameField
import com.senior25.tzakar.ui.presentation.components.image.LoadMediaImage
import com.senior25.tzakar.ui.presentation.components.loader.FullScreenLoader
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar
import com.senior25.tzakar.ui.presentation.dialog.edit_profile.ShowProfileUpdateSuccessDialog
import com.senior25.tzakar.ui.presentation.dialog.edit_profile.ShowSaveProfileConfirmation
import com.senior25.tzakar.ui.presentation.screen.main._page.MainPageEvent
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.theme.MyColors
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.edit_profile
import tzakar_reminder.composeapp.generated.resources.email_address
import tzakar_reminder.composeapp.generated.resources.enter_email_address
import tzakar_reminder.composeapp.generated.resources.enter_username
import tzakar_reminder.composeapp.generated.resources.gender
import tzakar_reminder.composeapp.generated.resources.ic_arrow_down
import tzakar_reminder.composeapp.generated.resources.ic_edit_pen
import tzakar_reminder.composeapp.generated.resources.ic_email
import tzakar_reminder.composeapp.generated.resources.ic_gender
import tzakar_reminder.composeapp.generated.resources.ic_person
import tzakar_reminder.composeapp.generated.resources.ic_profile_placeholder
import tzakar_reminder.composeapp.generated.resources.please_specify_your_gender
import tzakar_reminder.composeapp.generated.resources.save_changes
import tzakar_reminder.composeapp.generated.resources.username

//https://www.figma.com/design/fpM6aPdyAxvnmMpSeyMETh/150%2B-Pro-Avatars-Pack-%7C-3-Styles-%7C-Free-%7C-Male-%26-Female-Avatars-(Community)?node-id=1327-64809&t=mDrEd52gNTNcNeNs-0
class EditProfileScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val mainViewModel = koinScreenModel<MainScreenViewModel>()
        val screenModel = koinScreenModel<EditProfileViewModel>()

        val interaction= object :EditProfilePageInteraction{
            override fun onContinueClick() {
                screenModel.updateProfile {
                    onUIEvent(EditProfilePageEvent.Success)
                    onUIEvent(EditProfilePageEvent.UpdatePopUpState(EditProfilePagePopUp.SaveChangesSuccess))
                }
            }

            override fun getSelectedGender(): StateFlow<Int>? = screenModel.selectedGender

            override fun getAvatars(): AvatarsModel?  = screenModel.avatars

            override fun onUIEvent(event: EditProfilePageEvent) {
                screenModel.onUIEvent(event)
            }

            override fun getUiState(): StateFlow<EditProfilePageUiState?> = screenModel.uiState

            override fun getUsername(): String? = screenModel.username

            override fun getEmail(): String? = screenModel.email

            override fun getPopupState(): StateFlow<EditProfilePagePopUp?> = screenModel.popUpState

            override fun getImage(): StateFlow<String?> = screenModel.image

            override fun updateProfile() {
                mainViewModel.onUIEvent(MainPageEvent.UpdateProfile)
            }

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
private fun EditProfilePageScreen(paddingValues: PaddingValues,interaction: EditProfilePageInteraction?) {

    var expanded by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val screenHeight = getScreenHeight()
    var dropdownOffset by remember { mutableStateOf(0.dp) }

    var isValidUsername by remember { mutableStateOf(false) }

    val selectedGender: State<Int>? = interaction?.getSelectedGender()?.collectAsState()

    val isGenderValid by remember(selectedGender?.value) { mutableStateOf(
        selectedGender?.value!= -1 && selectedGender?.value != null
    ) }


    val keyboardController = LocalSoftwareKeyboardController.current
    val usernameFocusRequester = remember { FocusRequester() }
    val uiState =  interaction?.getUiState()?.collectAsState()
    val image =  interaction?.getImage()?.collectAsState()

    val popUpState = interaction?.getPopupState()?.collectAsState()

    val avatars by remember(selectedGender) {
        derivedStateOf {
            when (Genders.getByValue(selectedGender?.value)) {
                Genders.UNKNOWN -> emptyList()
                Genders.MALE -> interaction?.getAvatars()?.maleAvatars?.filter { it != "null" } ?: emptyList()
                Genders.FEMALE -> interaction?.getAvatars()?.femaleAvatars?.filter { it != "null" } ?: emptyList()
                Genders.NON_BINARY -> {
                    listOfNotNull(
                        interaction?.getAvatars()?.maleAvatars?.filter { it != "null" },
                        interaction?.getAvatars()?.femaleAvatars?.filter { it != "null" }
                    ).flatten()
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp).padding(bottom = 50.dp),
        shape =  RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MyColors.colorLightGrey),
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
                Box(modifier = Modifier.height(182.dp).padding(bottom = 16.dp)) {
                    LoadMediaImage(
                        modifier = Modifier
                            .size(150.dp)
                            .background(Color.White, CircleShape)
                            .clip(CircleShape),
                        url = image?.value,
                        default = Res.drawable.ic_profile_placeholder
                    )
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MyColors.colorPurple, CircleShape)
                            .align(Alignment.BottomCenter)
                            .offset(y = 16.dp)
                            .zIndex(1f),
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_edit_pen),
                            contentDescription = "",
                            modifier = Modifier
                                .matchParentSize()
                                .padding(4.dp)
                                .offset(y = (-16).dp)
                                .clip(CircleShape)
                                .clickable {
                                    if (interaction?.getAvatars() != null){
                                        expanded = !expanded
                                        dropdownOffset = calculateDropdownOffset(density, screenHeight)
                                    }
                                },
                            tint = MyColors.colorDarkBlue
                        )
                    }



                    DropdownMenu(
                        expanded = expanded &&   avatars.ifEmpty { null } != null,
                        onDismissRequest = { expanded = false },
                        offset = DpOffset(
                            x = with(density){ ((getScreenWidth() / 2) - (250.dp / 2))-32.dp },
                            y = dropdownOffset
                        ),
                        modifier = Modifier.width(250.dp).padding(horizontal = 8.dp).background(Color.White)
                    ) {

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.width(250.dp).height(200.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            itemsIndexed(items = avatars) { _, item ->
                                DropdownMenuItem(onClick = {
                                    if (item != image?.value)interaction?.onUIEvent(EditProfilePageEvent.UpdateImage(item))
                                    expanded = false
                                },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    LoadMediaImage(
                                        modifier = Modifier.size(80.dp)
                                            .background(Color.White, RoundedCornerShape(8.dp))
                                            .clip(RoundedCornerShape(8.dp)),
                                        url = item,
                                        default = Res.drawable.ic_profile_placeholder
                                    )
                                }
                            }
                        }
                    }
                }
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

                DropDownField<GenderModel>(
                    selectedItem = Gender.getGenders().firstOrNull { it.id == selectedGender?.value },
                    label = stringResource(Res.string.gender),
                    startIcon = painterResource(Res.drawable.ic_gender),
                    endIcon = painterResource(Res.drawable.ic_arrow_down),
                    onItemSelected={
                        interaction?.onUIEvent(EditProfilePageEvent.UpdateSelectedGender(it))
                    },
                    displayValue = {it?.value?:""},
                    isMandatory = true,
                    placeHolder= stringResource(Res.string.please_specify_your_gender),
                    items =  Gender.getGenders()
                )
            }


            Column(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomButton(
                    isEnabled = isValidUsername && isGenderValid  ,
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
            onDismiss = {
                interaction.updateProfile()
                interaction?.onBackPress()
            }
        )
    }
}

interface EditProfilePageInteraction: BackPressInteraction {
    fun onContinueClick()
    fun getSelectedGender(): StateFlow<Int>?
    fun getAvatars(): AvatarsModel?
    fun onUIEvent(event: EditProfilePageEvent)
    fun getUiState(): StateFlow<EditProfilePageUiState?>
    fun getUsername():String?
    fun getEmail():String?
    fun getPopupState(): StateFlow<EditProfilePagePopUp?>
    fun getImage(): StateFlow<String?>
    fun updateProfile()

}
