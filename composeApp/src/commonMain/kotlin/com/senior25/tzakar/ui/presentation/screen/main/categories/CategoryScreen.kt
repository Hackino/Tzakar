package com.senior25.tzakar.ui.presentation.screen.main.categories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.data.local.model.reminder.RingTones
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.platform_specific.map.MapView
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.CategoryType
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.CategoryType.Companion.categoryHeaderRes
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.fields.DateField
import com.senior25.tzakar.ui.presentation.components.fields.DropDownField
import com.senior25.tzakar.ui.presentation.components.fields.TimeField
import com.senior25.tzakar.ui.presentation.components.fields.normalTextField
import com.senior25.tzakar.ui.presentation.components.loader.FullScreenLoader
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar
import com.senior25.tzakar.ui.presentation.dialog.reminder_set.ShowAddReminderConfirmation
import com.senior25.tzakar.ui.presentation.dialog.reminder_set.ShowReminderAddedSuccessDialog
import com.senior25.tzakar.ui.presentation.dialog.reminder_set.ShowSelectValidDate
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.full_screen_map.FullScreenMap
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontParagraphM
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.description
import tzakar_reminder.composeapp.generated.resources.enter_description
import tzakar_reminder.composeapp.generated.resources.enter_title
import tzakar_reminder.composeapp.generated.resources.ic_arrow_down
import tzakar_reminder.composeapp.generated.resources.ic_pause
import tzakar_reminder.composeapp.generated.resources.ic_play
import tzakar_reminder.composeapp.generated.resources.please_specify_your_gender
import tzakar_reminder.composeapp.generated.resources.reminder_data
import tzakar_reminder.composeapp.generated.resources.reminder_time
import tzakar_reminder.composeapp.generated.resources.select_date
import tzakar_reminder.composeapp.generated.resources.select_time
import tzakar_reminder.composeapp.generated.resources.set_reminder
import tzakar_reminder.composeapp.generated.resources.title

data class CategoryScreen(val type:CategoryType = CategoryType.UNKNOWN): Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val mainViewModel = koinScreenModel<MainScreenViewModel>()

        val screenModel = koinScreenModel<CategoryViewModel>()

        val interaction = object :CategoryPageInteraction{
            override fun onContinueClick() {
                screenModel.setCategory(type.value) {
                    onUIEvent(CategoryPageEvent.Success)
                    onUIEvent(CategoryPageEvent.UpdatePopUpState(CategoryPagePopUp.SaveChangesSuccess))
                }
            }
            override fun onUIEvent(event: CategoryPageEvent) { screenModel.onUIEvent(event) }
            override fun getUiState(): StateFlow<CategoryPageUiState?> = screenModel.uiState
            override fun getPopupState(): StateFlow<CategoryPagePopUp?> = screenModel.popUpState
            override fun getReminderDate(): StateFlow<String?>  = screenModel.reminderDate
            override fun getReminderTime(): StateFlow<String?> =  screenModel.reminderTime
            override fun getTone(): StateFlow<String?>  = screenModel.sound
            override fun onBackPress() { navigator.pop() }
            override fun isPlaying():StateFlow<Boolean?>  = screenModel.isPlaying
            override fun getTabIndexState(): StateFlow<CategoryTabType?> = screenModel.tabIndexState
            override fun openMap(list: List<String>?) {
               navigator.push(FullScreenMap(list))
            }

            override fun getLongLat(): StateFlow<List<String>?> =  screenModel.longLat
        }

        Scaffold(
            topBar = { MyTopAppBar(type.categoryHeaderRes()?.let {  stringResource(it) }?:"" , showBack = true, interaction = interaction) },
            content = {paddingValues ->  CategoryPageScreen(paddingValues,interaction) },
        )
    }
}

@Composable
private fun CategoryPageScreen(paddingValues: PaddingValues,interaction: CategoryPageInteraction?) {

    val uiState =  interaction?.getUiState()?.collectAsState()
    val popUpState = interaction?.getPopupState()?.collectAsState()

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
        ) { showBirthdayScreen(interaction) }
    }
    if (uiState?.value is CategoryPageUiState.ProgressLoader) FullScreenLoader()

    if (popUpState?.value is CategoryPagePopUp.SaveChanges){
        ShowAddReminderConfirmation(
            onConfirm = { interaction.onContinueClick() },
            onDismiss = { interaction?.onUIEvent(CategoryPageEvent.UpdatePopUpState(CategoryPagePopUp.None)) }
        )
    }

    if (popUpState?.value is CategoryPagePopUp.SaveChangesSuccess) {
        ShowReminderAddedSuccessDialog(onDismiss = { interaction?.onBackPress() })
    }
    if (popUpState?.value is CategoryPagePopUp.SelectAValidDateBefore) {
        ShowSelectValidDate(onDismiss = {interaction?.onUIEvent(CategoryPageEvent.UpdatePopUpState(CategoryPagePopUp.None)) })
    }
}

@Composable
private fun ColumnScope.showBirthdayScreen(interaction: CategoryPageInteraction?){
    val tabState = interaction?.getTabIndexState()?.collectAsState()

    var isValidUsername by remember { mutableStateOf(false) }

    var isValidReminderData by remember { mutableStateOf(false) }

    var isValidReminderTime by remember { mutableStateOf(false) }

    var isValidLocation  by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    val usernameFocusRequester = remember { FocusRequester() }

    val descriptionFocusRequester = remember { FocusRequester() }

    val reminderTime = interaction?.getReminderTime()?.collectAsState()

    val reminderDate = interaction?.getReminderDate()?.collectAsState()
    val tone = interaction?.getTone()?.collectAsState()
    val isPlaying = interaction?.isPlaying()?.collectAsState()

    val getLongLat = interaction?.getLongLat()?.collectAsState()

    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        normalTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            label = stringResource(Res.string.title),
            placeHolder = stringResource(Res.string.enter_title),
            value = interaction?.getTitle(),
            onValueChange = { interaction?.onUIEvent(CategoryPageEvent.UpdateTitle(it)) },
            isInputValid = { isValidUsername = it },
            imeAction = ImeAction.Next,
            focusRequester = usernameFocusRequester,
            onKeyPressed = {
                descriptionFocusRequester.requestFocus();
                keyboardController?.hide()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        normalTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            label = stringResource(Res.string.description),
            placeHolder = stringResource(Res.string.enter_description),
            value = interaction?.getDescription(),
            onValueChange = { interaction?.onUIEvent(CategoryPageEvent.UpdateDescription(it)) },
            isInputValid = { isValidUsername = it },
            imeAction = ImeAction.Done,
            validate = false,
            isMandatory = false,
            onKeyPressed = {
                descriptionFocusRequester.freeFocus();
                keyboardController?.hide()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        DrawTabs(interaction)

        Spacer(modifier = Modifier.height(16.dp))

        if (tabState?.value == CategoryTabType.TIME){
            DateField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                label = stringResource(Res.string.reminder_data),
                placeHolder = stringResource(Res.string.select_date),
                value = reminderDate?.value,
                validate = true,
                onValueChange = { interaction.onUIEvent(CategoryPageEvent.UpdateReminderDate(it)) },
                isInputValid = {
                    println("valid time $isValidReminderTime and valid date $isValidReminderData")
                    isValidReminderData = it
                    isValidReminderTime = it && isValidReminderTime
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            TimeField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                label = stringResource(Res.string.reminder_time),
                placeHolder = stringResource(Res.string.select_time),
                value = reminderTime?.value,
                selectedDate =reminderDate?.value,
                onValueChange = { interaction?.onUIEvent(CategoryPageEvent.UpdateReminderTime(it)) },
                isInputValid = { isValidReminderTime = it },
                validate = true,
                validateDateBefore = {
                    if (!isValidReminderData){
                        interaction?.onUIEvent(CategoryPageEvent.UpdatePopUpState(CategoryPagePopUp.SelectAValidDateBefore))
                        false
                    }else{
                        true
                    }
                }
            )
        }else if (tabState?.value == CategoryTabType.LOCATION){
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    .height(150.dp)
                    .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { interaction.openMap(getLongLat?.value) }
            ) {

                MapView(
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    longLat = getLongLat?.value?.ifEmpty { null }?.map { it.toDoubleOrNull()?:0.0 }?: listOf(35.5018,33.8938)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DropDownField(
            selectedItem = RingTones.getAllRingTones().firstOrNull { it == tone?.value },
            label = "Tone",
            startIcon = if (isPlaying?.value == true)
                painterResource(Res.drawable.ic_pause) else painterResource(Res.drawable.ic_play),
            endIcon = painterResource(Res.drawable.ic_arrow_down),
            onItemSelected={
                interaction?.onUIEvent(CategoryPageEvent.UpdateReminderTone(it))
            },
            displayValue = {it?.replace("_"," ")?:""},
            isMandatory = true,
            placeHolder= stringResource(Res.string.please_specify_your_gender),
            items =  RingTones.getAllRingTones(),
            startIconClick = {
                interaction?.onUIEvent(CategoryPageEvent.UpdatePlayingStatus)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

    Column(
        modifier = Modifier.padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        println("for button ${isValidUsername}  ${isValidReminderData}   ${isValidReminderTime} ")
        val isValid = if (tabState?.value == CategoryTabType.TIME){
            isValidUsername && isValidReminderData && isValidReminderTime
        }else{
            isValidUsername && isValidLocation
        }
        CustomButton(
            isEnabled = isValid,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            onClick = { interaction?.onUIEvent(CategoryPageEvent.UpdatePopUpState(CategoryPagePopUp.SaveChanges)) },
            text = stringResource(Res.string.set_reminder)
        )
    }
}

@Composable
private fun DrawTabs(
    interaction: CategoryPageInteraction?,
) {
    val tabState = interaction?.getTabIndexState()?.collectAsState()
    TabsRow(
        selectedTab = tabState?.value?.value ?: 0,
        tabs = listOf("DateTime", "Location"),
        onClick = { index ->
            if (index == 0) {
                interaction?.onUIEvent(CategoryPageEvent.TimeBased)
            } else if (index == 1) {
                interaction?.onUIEvent(CategoryPageEvent.LocationBased)
            }
        }
    )
}

@Composable
fun TabsRow(
    selectedTab:Int?=0,
    tabs:List<String>,
    onClick: (index : Int) -> Unit
) {
    val newTabSelected = remember { mutableStateOf(selectedTab) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(end = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        tabs.forEachIndexed { index, title ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    newTabSelected.value = index
                    onClick(index)
                }
            ) {
                RadioButton(
                    selected = index == selectedTab,
                    onClick = { newTabSelected.value = index;onClick(index) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black,
                        unselectedColor = Color.Gray
                    )
                )

                Text(
                    text = title,
                    style = fontParagraphM,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

interface CategoryPageInteraction: BackPressInteraction {
    fun onContinueClick(){}
    fun onUIEvent(event: CategoryPageEvent){}
    fun getUiState(): StateFlow<CategoryPageUiState?>
    fun getPopupState(): StateFlow<CategoryPagePopUp?>
    fun getTitle(): String? = ""
    fun getDescription(): String? = ""
    fun getReminderDate(): StateFlow<String?>
    fun getReminderTime(): StateFlow<String?>
    fun getTone(): StateFlow<String?>
    fun isPlaying():StateFlow<Boolean?>
    fun getTabIndexState(): StateFlow<CategoryTabType?>
    fun openMap(list:List<String>?)
    fun getLongLat():StateFlow<List<String>?>

}