package com.senior25.tzakar.ui.presentation.screen.main.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.data.local.model.menu.MenuModel
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.ui.presentation.app.AppNavigator
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.CategoriesFiltersBottomSheet
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.CategoriesFiltersSheetInteraction
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.CategoryType
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.CategoryType.Companion.categoryRes
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.button.OutlinedCustomButton
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar
import com.senior25.tzakar.ui.presentation.screen.common.composable.no_data.NoDataWidget
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.category_details.CategoryDetailsScreen
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH1
import com.senior25.tzakar.ui.theme.fontH3
import com.senior25.tzakar.ui.theme.fontHighlight
import com.senior25.tzakar.ui.theme.fontLink
import com.senior25.tzakar.ui.theme.fontParagraphL
import com.senior25.tzakar.ui.theme.fontParagraphS
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.calendar
import tzakar_reminder.composeapp.generated.resources.filter

object CalendarTab: Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.DateRange)
            val title  = "Calendar"
            val index:UShort= 1u
            return TabOptions(icon = icon,title = title, index = index)
        }

    @Composable
    override fun Content() {
        Navigator(CalendarScreen()){  SlideTransition(it) }
    }
}

class CalendarScreen: Screen {

    @Composable
    override fun Content() {
        AppNavigator.addTabNavigator(LocalNavigator.current)
        val viewModel = koinScreenModel<CalendarViewModel>()
        val mainViewModel = koinScreenModel<MainScreenViewModel>()
        val navigator = LocalNavigator.currentOrThrow

        val interaction  = object : CalendarScreenInteraction {

            override fun getUiState(): StateFlow<CalendarPageUiState?> = viewModel.uiState

            override fun onUIEvent(event: CalendarPageEvent) { viewModel.onUIEvent(event) }

            override fun navigate(action: NavigationAction) {}

            override fun getSelectedMonth(): StateFlow<Month?>  = viewModel.selectedMonth

            override fun getSelectedYear(): StateFlow<Int?> = viewModel.selectedYear

            override fun getSelectedDate(): StateFlow<Int?> = viewModel.selectedDate

            override fun getMonthDates(): StateFlow<List<Pair<Int,String>>?> =  viewModel.monthDates

            override fun getShouldScroll(): SharedFlow<Boolean?> = viewModel.shouldAutoScroll

            override fun getPopupState(): StateFlow<CalendarPagePopUp?> = viewModel.popUpState

            override fun getAllMenuFilters(): StateFlow<MutableList<MenuModel>?>  =  viewModel.selectedFilters

            override fun getAllSorting(): StateFlow<MutableList<MenuModel>?>  =  viewModel.selectedSorting

            override fun getFilteredReminder(): StateFlow<List<ReminderModel>?>  = viewModel.filteredReminders

            override fun updateReminderStatus(reminderModel: ReminderModel?) {
                viewModel.onUIEvent(CalendarPageEvent.UpdateReminderStatus(reminderModel))
            }

            override fun applyCategoriesFilters(filters: List<MenuModel>,sorting:List<MenuModel>) {
                viewModel.updateFilter(filters,sorting)
            }

            override fun resetPerksFilters() {
                viewModel.resetFilters()
            }
        }

        Scaffold(
            backgroundColor = MyColors.colorOffWhite,
            topBar = { MyTopAppBar( stringResource(Res.string.calendar), showBack = false, centerTitle = false) },
            content = { CalendarScreen(interaction) }
        )
    }

    @Composable
    fun CalendarScreen(interaction: CalendarScreenInteraction){

        val navigator = LocalNavigator.currentOrThrow

        val popUpState = interaction.getPopupState().collectAsState()

        val reminders = interaction.getFilteredReminder().collectAsState()

        val filters = interaction.getAllMenuFilters().collectAsState()

        val sorting = interaction.getAllSorting().collectAsState()

        val selectedMonth = interaction.getSelectedMonth().collectAsState()

        val selectedYear = interaction.getSelectedYear().collectAsState()

        val selectedDate = interaction.getSelectedDate().collectAsState()

        val monthDays = interaction.getMonthDates().collectAsState()

        var showDialog by remember { mutableStateOf(false) }

        if (popUpState.value is CalendarPagePopUp.ShowCategoriesFilterSheet){
            CategoriesFiltersBottomSheet(
                selectedFilters = filters.value,
                selectedSorting = sorting.value,
                interaction = interaction,
                onDismiss = { interaction.onUIEvent(CalendarPageEvent.UpdatePopUpState(CalendarPagePopUp.None)) }
            )
        }

        if (showDialog) {
            MonthYearPickerDialog(
                selectedMonth = selectedMonth.value?:Month.JANUARY,
                selectedYear = selectedYear.value ?: 2025,
                onMonthYearSelected = { month, year ->
                    interaction.onUIEvent(CalendarPageEvent.UpdateMonthYear(month,year))
                },
                onDismiss = { showDialog = false }
            )
        }

        val listState = rememberLazyListState()

        LaunchedEffect(key1 = Unit) { interaction.onUIEvent(CalendarPageEvent.Init) }

        LaunchedEffect(interaction.getShouldScroll()) {
            interaction.getShouldScroll().collectLatest { shouldScroll ->
                if ( shouldScroll == true ) {
                    selectedDate.value?.let { selectedDay ->
                        val index = monthDays.value?.map { it.first }?.indexOf(selectedDay) ?: -1
                        if (index != -1) listState.scrollToItem(index)
                    }
                    interaction.onUIEvent(CalendarPageEvent.RemoveAutoScroll)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                Column(modifier = Modifier) {
                    Spacer(Modifier.height(16.dp))
                    Row(modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable(enabled = true){ showDialog = true },
                        verticalAlignment =Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedMonth.value?.name?.substring(0,3) +", " + selectedYear.value.toString() +" ▼"  ,
                            textAlign = TextAlign.Start,
                            color = MyColors.colorDarkBlue,
                            style = fontH3,
                            modifier = Modifier
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    LazyRow(
                        state = listState,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(monthDays.value?: emptyList()){ index, int->
                            DateBox(date = int,selectedDate = selectedDate.value){
                                interaction.onUIEvent(CalendarPageEvent.UpdateDayDate(it))
                            }
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${if (filters?.value?.isNotEmpty() == true) "Filtered - " else ""}Reminders",
                            textAlign = TextAlign.Start,
                            color = MyColors.colorDarkBlue,
                            style = fontH3,
                            modifier = Modifier
                        )
                        CustomButton(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = "Filter",
                            endIcon = painterResource(Res.drawable.filter),
                            buttonColor = MyColors.colorPurple,
                            onClick = {
                                interaction.onUIEvent(
                                    CalendarPageEvent.UpdatePopUpState(
                                        CalendarPagePopUp.ShowCategoriesFilterSheet
                                    )
                                )
                            },
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }

            reminders.value?.ifEmpty { null }?.let {
                itemsIndexed(it){_,item->
                    ReminderItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        reminderModel =  item,
                        isSelected = item.isEnabled == 1,
                        onSelect = { interaction.updateReminderStatus(it) }
                    ){ navigator.push(CategoryDetailsScreen(it?.id)) }
                    Spacer(Modifier.height(8.dp))
                }
            }?:run {
                item {
                    NoDataWidget(modifier = Modifier.fillMaxWidth().padding(top = 128.dp).height(200.dp))
                }
            }

        }

    }

    @Composable
    private fun DateBox(
        date: Pair<Int,String>,
        selectedDate: Int? = null,
        onDateSelect: (Int) -> Unit
    ) {
        val selected: Int? by remember(selectedDate) { mutableStateOf(selectedDate) }
        Box(
            modifier = Modifier.width(90.dp).height(110.dp)
                .border(
                    2.dp,
                    Color.Gray,
                    RoundedCornerShape(24.dp)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(if (date.first == selected) MyColors.colorDarkBlue
                else Color.Transparent,)
                .clickable { onDateSelect(date.first) }
        ) {
            Text(
                text = date.second.substring(0,3) +"\n"+ date.first,
                maxLines = 3,
                style = fontHighlight,
                color =      if (date.first == selected) MyColors.colorWhite
                else Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp).align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
    }

    interface CalendarScreenInteraction: CategoriesFiltersSheetInteraction {
        fun getUiState(): StateFlow<CalendarPageUiState?>
        fun onUIEvent(event: CalendarPageEvent)
        fun navigate(action: NavigationAction)
        fun getSelectedMonth(): StateFlow<Month?>
        fun getSelectedYear(): StateFlow<Int?>
        fun getSelectedDate(): StateFlow<Int?>
        fun getMonthDates(): StateFlow<List<Pair<Int,String>>?>
        fun getShouldScroll(): SharedFlow<Boolean?>
        fun getPopupState(): StateFlow<CalendarPagePopUp?>
        fun getAllMenuFilters(): StateFlow<MutableList<MenuModel>?>
        fun getAllSorting(): StateFlow<MutableList<MenuModel>?>
        fun getFilteredReminder(): StateFlow<List<ReminderModel>?>
        fun updateReminderStatus(reminderModel: ReminderModel?)

    }

    enum class NavigationAction {
        REMINDER_DETAILS
    }
}


@Composable
fun MonthYearPickerDialog(
    selectedMonth: Month,
    selectedYear: Int,
    onMonthYearSelected: (Month, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var year by remember { mutableStateOf(selectedYear) }
    var month by remember { mutableStateOf(selectedMonth) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Select Month & Year"  ,
                textAlign =  TextAlign.Start,
                color = MyColors.colorDarkBlue,
                style = fontLink.copy(fontSize = 24.sp),
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(200.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    itemsIndexed(Month.entries) { index,m ->
                        Text(
                            modifier = Modifier
                                .border(
                                    2.dp,
                                    if (m == month) MyColors.colorDarkBlue
                                    else Color.Transparent,
                                    RoundedCornerShape(24.dp)
                                )
                                .padding(2.dp)
                                .clickable { month = m }.padding(4.dp),
                            text = m.name.lowercase().substring(0,3).replaceFirstChar { it.uppercase() },
                            color = MyColors.colorDarkBlue,
                            textAlign = TextAlign.Center,
                            style = fontParagraphS.copy(fontSize = 20.sp)
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { year-- }) { Text("◀") }
                    Text(
                        "$year" ,
                        color = MyColors.colorDarkBlue,
                        style = fontParagraphS.copy(fontSize = 24.sp),
                    )
                    IconButton(onClick = { year++ }) { Text("▶") }
                }
            }
        },
        confirmButton = {
            CustomButton(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "CONFIRM",
                buttonColor = MyColors.colorPurple,
                onClick = { onMonthYearSelected(month, year);onDismiss() },
            )
        },
        dismissButton = {
            OutlinedCustomButton(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "CANCEL",
                onClick = onDismiss,
            )
        }
    )
}

@Composable
fun ReminderItem(
    modifier: Modifier,
    reminderModel: ReminderModel? = null,
    isSelected:Boolean? = false,
    onSelect:(ReminderModel?)->Unit = {  },
    onClick:(ReminderModel?) ->Unit = {}
) {
    var isChecked by remember(isSelected) { mutableStateOf(isSelected == true) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MyColors.colorWhite)
            .clickable { onClick.invoke(reminderModel) }
            .padding(bottom = 8.dp)
            .padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RectangleShape)
                .then(Modifier.padding(vertical = 8.dp))
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = CategoryType.getByValue(reminderModel?.type).categoryRes()?.let { stringResource(it) }?:"",
                style = fontParagraphL,
                color = MyColors.colorDarkBlue,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.size(52.dp, 32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isChecked) MyColors.colorDarkBlue else MyColors.colorLightGrey)
                    .clickable { isChecked = !isChecked; onSelect(reminderModel?.copy(isEnabled = if (isChecked)1 else 0)) }
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

        Text(
            text = reminderModel?.time?:"",
            style = fontH1,
            color = MyColors.colorDarkBlue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = reminderModel?.title?:"",
            style = fontH3,
            color = MyColors.colorDarkBlue,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        reminderModel?.description?.let {
            Text(
                text = it,
                style = fontParagraphL,
                color = MyColors.colorDarkBlue,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
