package com.senior25.tzakar.ui.presentation.screen.main.categories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.CategoryType
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.fields.DateField
import com.senior25.tzakar.ui.presentation.components.fields.TimeField
import com.senior25.tzakar.ui.presentation.components.fields.normalTextField
import com.senior25.tzakar.ui.presentation.components.loader.FullScreenLoader
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar
import com.senior25.tzakar.ui.presentation.dialog.reminder_set.ShowAddReminderConfirmation
import com.senior25.tzakar.ui.presentation.dialog.reminder_set.ShowReminderAddedSuccessDialog
import com.senior25.tzakar.ui.presentation.dialog.reminder_set.ShowSelectValidDate
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.theme.MyColors
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Custom
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.add_Birthday
import tzakar_reminder.composeapp.generated.resources.add_bills
import tzakar_reminder.composeapp.generated.resources.add_games
import tzakar_reminder.composeapp.generated.resources.add_medication
import tzakar_reminder.composeapp.generated.resources.add_store
import tzakar_reminder.composeapp.generated.resources.add_tv_movies
import tzakar_reminder.composeapp.generated.resources.description
import tzakar_reminder.composeapp.generated.resources.enter_description
import tzakar_reminder.composeapp.generated.resources.enter_title
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

            override fun onBackPress() { navigator.pop() }
        }

        val title = when(type){
            CategoryType.STORE -> Res.string.add_store
            CategoryType.TV ->Res.string.add_tv_movies
            CategoryType.GAMES -> Res.string.add_games
            CategoryType.BIRTHDAY -> Res.string.add_Birthday
            CategoryType.BILLS ->Res.string.add_bills
            CategoryType.MEDICATION -> Res.string.add_medication
            CategoryType.CUSTOM -> Res.string.Custom
            else->null
        }
        Scaffold(
            topBar = { MyTopAppBar(title?.let {  stringResource(it) }?:"" , showBack = true, interaction = interaction) },
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

    var isValidUsername by remember { mutableStateOf(false) }

    var isValidReminderData by remember { mutableStateOf(false) }

    var isValidReminderTime by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    val usernameFocusRequester = remember { FocusRequester() }

    val descriptionFocusRequester = remember { FocusRequester() }

    val reminderTime = interaction?.getReminderTime()?.collectAsState()

    val reminderDate = interaction?.getReminderDate()?.collectAsState()

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

        DateField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            label = stringResource(Res.string.reminder_data),
            placeHolder = stringResource(Res.string.select_date),
            value = reminderDate?.value,
            validate = true,
            onValueChange = { interaction?.onUIEvent(CategoryPageEvent.UpdateReminderDate(it)) },
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

        //Tone
//        DateTimeField(
//            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
//            label = stringResource(Res.string.reminder_data_time),
//            placeHolder = stringResource(Res.string.select_date_and_time),
//            value = interaction?.getDescription(),
//            onValueChange = { interaction?.onUIEvent(CategoryPageEvent.UpdateReminderDate(it)) },
//            isInputValid = { isValidReminderData = it },
//        )
        Spacer(modifier = Modifier.height(16.dp))
    }

    Column(
        modifier = Modifier.padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        println("for button ${isValidUsername}  ${isValidReminderData}   ${isValidReminderTime} ")
        CustomButton(
            isEnabled = isValidUsername && isValidReminderData && isValidReminderTime   ,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            onClick = { interaction?.onUIEvent(CategoryPageEvent.UpdatePopUpState(CategoryPagePopUp.SaveChanges)) },
            text = stringResource(Res.string.set_reminder)
        )
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


}