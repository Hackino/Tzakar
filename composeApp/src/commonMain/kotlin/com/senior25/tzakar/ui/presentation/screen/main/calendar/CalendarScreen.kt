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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.ui.presentation.app.AppNavigator
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.button.OutlinedCustomButton
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.profile.NavigationAction
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH3
import com.senior25.tzakar.ui.theme.fontHighlight
import com.senior25.tzakar.ui.theme.fontLink
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
import tzakar_reminder.composeapp.generated.resources.ic_back

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
        }
        Scaffold(
            backgroundColor = MyColors.colorOffWhite,
            topBar = { MyTopAppBar( stringResource(Res.string.calendar), showBack = false, centerTitle = false) },
            content = { CalendarScreen(interaction) }
        )
    }

    @Composable
    fun CalendarScreen(interaction: CalendarScreenInteraction){

        val selectedMonth =   interaction.getSelectedMonth().collectAsState()

        val selectedYear =   interaction.getSelectedYear().collectAsState()

        val selectedDate =   interaction.getSelectedDate().collectAsState()

        val monthDays =   interaction.getMonthDates().collectAsState()

        var showDialog by remember { mutableStateOf(false) }

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

        LaunchedEffect(interaction.getShouldScroll()) {
            interaction.getShouldScroll().collectLatest { shouldScroll ->
                if (shouldScroll == true){
                    selectedDate.value?.let { selectedDay ->
                        val index = monthDays.value?.map { it.first }?.indexOf(selectedDay) ?: -1
                        if (index != -1) listState.scrollToItem(index)
                    }
                }
            }
        }

        LaunchedEffect(key1 = Unit) {interaction.onUIEvent(CalendarPageEvent.Init) }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                    Row(modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                        verticalAlignment =Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Reminders",
                            textAlign =  TextAlign.Start,
                            color = MyColors.colorDarkBlue,
                            style = fontH3,
                            modifier = Modifier
                        )

                        CustomButton(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = "Filter",
                            endIcon = painterResource(Res.drawable.filter),
                            buttonColor = MyColors.colorPurple,
                            onClick = {  },
                        )
                    }

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


    interface CalendarScreenInteraction{
        fun getUiState(): StateFlow<CalendarPageUiState?>
        fun onUIEvent(event: CalendarPageEvent)
        fun navigate(action: NavigationAction)
        fun getSelectedMonth(): StateFlow<Month?>
        fun getSelectedYear(): StateFlow<Int?>
        fun getSelectedDate(): StateFlow<Int?>
        fun getMonthDates(): StateFlow<List<Pair<Int,String>>?>
        fun getShouldScroll(): SharedFlow<Boolean?>

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
                            style = fontParagraphS.copy(fontSize = 20.sp),

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