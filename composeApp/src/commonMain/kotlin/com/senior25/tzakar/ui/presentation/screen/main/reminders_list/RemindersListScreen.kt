package com.senior25.tzakar.ui.presentation.screen.main.reminders_list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.ui.presentation.app.AppNavigator
import com.senior25.tzakar.ui.presentation.components.loader.FullScreenLoader
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar
import com.senior25.tzakar.ui.presentation.screen.common.composable.no_data.NoDataWidget
import com.senior25.tzakar.ui.presentation.screen.main._page.MainPageEvent
import com.senior25.tzakar.ui.presentation.screen.main._page.MainPagePopUp
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.calendar.ReminderItem
import com.senior25.tzakar.ui.presentation.screen.main.category_details.CategoryDetailsScreen
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH3
import com.senior25.tzakar.ui.theme.fontParagraphM
import kotlinx.coroutines.flow.StateFlow

class RemindersListScreen: Screen {
    @Composable
    override fun Content() {
        AppNavigator.addTabNavigator(LocalNavigator.current)
        val screenModel = koinScreenModel<RemindersListScreenViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        val mainViewModel = koinScreenModel<MainScreenViewModel>()

        val interaction  = object : RemindersListScreenInteraction {
            override fun getUiState(): StateFlow<RemindersListPageUiState?> = screenModel.uiState
            override fun getCurrentDate(): String = mainViewModel.getCurrentDate()
            override fun getTabIndexState(): StateFlow<ReminderTabType?> = screenModel.tabIndexState
            override fun onUIEvent(event: RemindersListPageEvent) {
                if(event == RemindersListPageEvent.Refresh){
                    mainViewModel.onUIEvent(MainPageEvent.Refresh)
                }
                screenModel.onUIEvent(event)
            }
            override fun navigate(action: RemindersListNavigationAction) {
                when(action){
                    RemindersListNavigationAction.ReminderDetail -> {}
                    RemindersListNavigationAction.AddReminder ->{
                        mainViewModel.onUIEvent(MainPageEvent.UpdatePopUpState(MainPagePopUp.CategoriesSheet))
                    }
                }
            }
            override fun getFilteredReminder(): StateFlow<Map<String, List<ReminderModel>>?>  = screenModel.filteredReminders

            override fun updateReminderStatus(reminderModel: ReminderModel?) {
                screenModel.onUIEvent(RemindersListPageEvent.UpdateReminderStatus(reminderModel))
            }


            override fun onBackPress() {
                navigator.pop()
            }
        }

        Scaffold(
            backgroundColor = MyColors.colorOffWhite,
            topBar = { MyTopAppBar("ALL Reminders" , showBack = true,interaction= interaction) },
            content = { RemindersListScreen(interaction) }
        )
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun RemindersListScreen(interaction: RemindersListScreenInteraction){
        val uiState = interaction.getUiState()?.collectAsState()
        val listState = rememberLazyListState()
        val navigator = LocalNavigator.currentOrThrow


        LaunchedEffect(key1 = Unit) {
            interaction.onUIEvent(RemindersListPageEvent.Init)
        }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = uiState?.value is RemindersListPageUiState.Refreshing,
            onRefresh =   {
                if (uiState?.value !is RemindersListPageUiState.Refreshing
                    && uiState?.value != RemindersListPageUiState.Loading
                ) interaction.onUIEvent(RemindersListPageEvent.Refresh)
            }
        )

        val reminders = interaction.getFilteredReminder().collectAsState()
        Box(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .background(MyColors.colorOffWhite)
        ) {
            if (uiState?.value is RemindersListPageUiState.Refreshing || uiState?.value == RemindersListPageUiState.Loading) {
                FullScreenLoader()
            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 120.dp),
                    state = listState
                ) {
                    item{Spacer(Modifier.height(16.dp))}
                    item{ DrawTabs(interaction) }
                    item{Spacer(Modifier.height(16.dp))}
                    reminders.value?.ifEmpty { null }?.let {map->
                        map.keys.forEach {
                            (map[it]?: emptyList()).let { reminders->
                                item{
                                    Text(
                                        text = it ,
                                        textAlign = TextAlign.Start,
                                        color = MyColors.colorDarkBlue,
                                        style = fontH3,
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                                    )
                                    Spacer(Modifier.height(8.dp))
                                }

                                itemsIndexed(reminders) { _, item ->
                                    ReminderItem(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        reminderModel = item,
                                        isSelected = item.isEnabled == 1,
                                        onSelect = { interaction.updateReminderStatus(it) }
                                    ) { navigator.push(CategoryDetailsScreen(it?.id)) }
                                    Spacer(Modifier.height(8.dp))
                                }
                            }

                        }
                    }?:run {
                        item {
                            NoDataWidget(modifier = Modifier.fillMaxWidth().padding(top = 128.dp).height(200.dp))
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = uiState?.value is RemindersListPageUiState.Refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }


    @Composable
    private fun DrawTabs(
        interaction: RemindersListScreenInteraction?,
    ) {
        val tabState = interaction?.getTabIndexState()?.collectAsState()
        TabsRow(
            selectedTab = tabState?.value?.value ?: 0,
            tabs = listOf("Active", "Completed"),
            onClick = { index ->
                if (index == 0) {
                    interaction?.onUIEvent(RemindersListPageEvent.LoadCurrent)
                } else if (index == 1) {
                    interaction?.onUIEvent(RemindersListPageEvent.LoadExpired)
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabs.forEachIndexed { index, title ->
                Box(
                    modifier = Modifier
                        .border(
                            width = if (index == selectedTab) 2.dp else 0.dp,
                            color = if (index == selectedTab) Color.Black else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            newTabSelected.value = index
                            onClick(index)
                        }
                ) {
                    Text(
                        text = title,
                        style = fontParagraphM,
                        color =  Color.Black ,
                        modifier = Modifier  .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }

    interface RemindersListScreenInteraction: BackPressInteraction {
        fun getUiState(): StateFlow<RemindersListPageUiState?>
        fun onUIEvent(event: RemindersListPageEvent)
        fun getCurrentDate(): String?
        fun getTabIndexState(): StateFlow<ReminderTabType?>
        fun navigate(action:RemindersListNavigationAction)
        fun getFilteredReminder(): StateFlow<Map<String, List<ReminderModel>>?>
        fun updateReminderStatus(reminderModel: ReminderModel?)
    }

    enum class RemindersListNavigationAction{
        ReminderDetail,AddReminder
    }
}