package com.senior25.tzakar.ui.presentation.screen.main.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.ui.presentation.app.AppNavigator
import com.senior25.tzakar.ui.presentation.components.image.LoadMediaImage
import com.senior25.tzakar.ui.presentation.components.loader.FullScreenLoader
import com.senior25.tzakar.ui.presentation.screen.common.composable.no_data.NoDataWidget
import com.senior25.tzakar.ui.presentation.screen.main._page.MainPageEvent
import com.senior25.tzakar.ui.presentation.screen.main._page.MainPagePopUp
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.calendar.CalendarPageEvent
import com.senior25.tzakar.ui.presentation.screen.main.calendar.ReminderItem
import com.senior25.tzakar.ui.presentation.screen.main.category_details.CategoryDetailsScreen
import com.senior25.tzakar.ui.presentation.screen.main.profile.NavigationAction
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH2
import com.senior25.tzakar.ui.theme.fontH3
import com.senior25.tzakar.ui.theme.fontParagraphM
import kotlinx.coroutines.flow.StateFlow
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.ic_profile_placeholder

object HomeTab: Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Home)
            val title  = "Home"
            val index:UShort= 1u
            return TabOptions(icon = icon,title = title, index = index)
        }

    @Composable
    override fun Content() {
        Navigator(HomeScreen()){  SlideTransition(it) }
    }
}

class HomeScreen: Screen {
    @Composable
    override fun Content() {
        AppNavigator.addTabNavigator(LocalNavigator.current)
        val screenModel = koinScreenModel<HomeScreenViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        val mainViewModel = koinScreenModel<MainScreenViewModel>()

        val interaction  = object : HomeScreenInteraction {
            override fun getUiState(): StateFlow<HomePageUiState?> = screenModel.uiState
            override fun getProfileState(): StateFlow<UserProfile?> = mainViewModel.userProfile
            override fun getCurrentDate(): String? = mainViewModel.getCurrentDate()
            override fun getTabIndexState(): StateFlow<ReminderTabType?> = screenModel.tabIndexState
            override fun onUIEvent(event: HomePageEvent) { screenModel.onUIEvent(event) }
            override fun navigate(action: HomeNavigationAction) {
                when(action){
                    HomeNavigationAction.ReminderDetail -> {}
                    HomeNavigationAction.AddReminder ->{
                        mainViewModel.onUIEvent(MainPageEvent.UpdatePopUpState(MainPagePopUp.CategoriesSheet))
                    }
                }
            }
            override fun getFilteredReminder(): StateFlow<List<ReminderModel>?>  = screenModel.filteredReminders
            override fun updateReminderStatus(reminderModel: ReminderModel?) {
                screenModel.onUIEvent(HomePageEvent.UpdateReminderStatus(reminderModel))
            }

            override fun countToday(): StateFlow<Int>  = screenModel.todayReminderCount
            override fun countTodayCompleted(): StateFlow<Int> =screenModel.todayCompletedReminderCount
            override fun countTotal(): StateFlow<Int> = screenModel.totalReminderCount
            override fun countTotalCompleted(): StateFlow<Int> = screenModel.totalCompleteReminderCount
        }

        Scaffold(
            backgroundColor = MyColors.colorOffWhite,
            topBar = {
                TopAppBar(
                    elevation = 0.dp,
                    title = {
                        Column {
                            interaction.getCurrentDate()?.let {
                                Text(
                                    it,
                                    textAlign =  TextAlign.Start,
                                    color = MyColors.colorDarkBlue,
                                    style = fontParagraphM,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Text(
                                "Hello, ${interaction.getProfileState().value?.userName?:""}",
                                textAlign =  TextAlign.Start,
                                color = MyColors.colorDarkBlue,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = fontH2,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    actions = {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            LoadMediaImage(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(Color.White, CircleShape)
                                    .clip(CircleShape),
                                url =interaction.getProfileState().value?.image,
                                default = Res.drawable.ic_profile_placeholder
                            )
                        }
                    },
                    backgroundColor = MyColors.colorWhite
                )
            },
            content = { HomeScreen(interaction) }
        )
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun HomeScreen(interaction: HomeScreenInteraction){
        val uiState = interaction.getUiState()?.collectAsState()
        val listState = rememberLazyListState()
        val navigator = LocalNavigator.currentOrThrow


        LaunchedEffect(key1 = Unit) {
            println("init called")
            interaction.onUIEvent(HomePageEvent.Init)
        }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = uiState?.value is HomePageUiState.Refreshing,
            onRefresh =   {
                if (uiState?.value !is HomePageUiState.Refreshing
                    && uiState?.value != HomePageUiState.Loading
                ) interaction.onUIEvent(HomePageEvent.Refresh)
            }
        )

        val reminders = interaction.getFilteredReminder().collectAsState()

        Box(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .background(MyColors.colorOffWhite)
        ) {
            if (uiState?.value is HomePageUiState.Refreshing || uiState?.value == HomePageUiState.Loading) {
                FullScreenLoader()
            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 120.dp),
                    state = listState
                ) {
                    item {
                        TopItems(
                            interaction = interaction,
                            pageData = uiState?.value?.data,
                        )
                    }

                    item{ Spacer(modifier = Modifier.height(8.dp)) }

                    item{ DrawTabs(interaction) }

                    item{Spacer(Modifier.height(16.dp))}
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
            PullRefreshIndicator(
                refreshing = uiState?.value is HomePageUiState.Refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }

    @Composable
    fun TopItems(
        topPadding: Dp = 0.dp,
        pageData: HomePageData?,
        interaction: HomeScreenInteraction?,
    ){
        val todayCount = interaction?.countToday()?.collectAsState()
        val todayCompletedCount = interaction?.countTodayCompleted()?.collectAsState()
        val totalCount = interaction?.countTotal()?.collectAsState()
        val totalCompletedCount = interaction?.countTotalCompleted()?.collectAsState()

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp,))
                .background(MyColors.colorWhite)
                .padding(top = 8.dp)
        ) {

            Column(modifier = Modifier.fillMaxWidth().padding(top = topPadding)) {

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    val progress = calculateProgress(todayCount?.value?:0 , todayCompletedCount?.value?:0)
                    HalfRingDashedProgressBar(
                        progress =progress,
                        modifier = Modifier.weight(2f),
                        strokeWidth = 12.dp,
                        dashLength = 390f,
                        gapLength = 0f,
                        textBelow = "Today's Reminders",
                    ){
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${todayCompletedCount?.value?:0}",
                                style = fontParagraphM,
                                color =  MyColors.colorDarkBlue ,
                                modifier = Modifier  .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            Box(
                                modifier = Modifier.height(2.dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                                    .background(MyColors.colorDarkBlue)
                            )
                            Text(
                                text = "${todayCount?.value?:0}",
                                style = fontH3,
                                color =  MyColors.colorDarkBlue ,
                                modifier = Modifier  .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    val progress2 = calculateProgress(totalCount?.value?:0 , totalCompletedCount?.value?:0)

                    HalfRingDashedProgressBar(
                        progress =progress2,
                        modifier = Modifier.weight(3f),
                        strokeWidth = 12.dp,
                        dashLength = 390f,
                        gapLength = 0f,
                        textBelow = "Total Reminders",
                        ){
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            Text(
                                text = "${totalCompletedCount?.value?:0}",
                                style = fontParagraphM,
                                color =  MyColors.colorDarkBlue ,
                                modifier = Modifier  .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            Box(
                                modifier = Modifier.height(2.dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                                    .background(MyColors.colorDarkBlue)
                            )
                            Text(
                                text = "${totalCount?.value?:0}",
                                style = fontH3,
                                color =  MyColors.colorDarkBlue ,
                                modifier = Modifier  .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(24.dp))

                    HalfRingDashedProgressBar(
                        progress =100f,
                        totalSweepAngle = 360f,
                        startAngle = 0f,
                        onClick = { interaction?.navigate(HomeNavigationAction.AddReminder) },
                        modifier = Modifier.weight(2f),
                        strokeWidth = 12.dp,
                        dashLength = 390f,
                        gapLength = 0f,
                        textBelow = "Add Reminder",
                        ){
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Image(
                                imageVector = Icons.Default.Add,
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(24.dp).clip(CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun calculateProgress(totalPoints: Int, pendingAmount: Int): Float {
        if (totalPoints == 0) return 0f // Prevent division by zero

        val result = (pendingAmount.toFloat() * 100f) / totalPoints.toFloat()

        return result.coerceIn(0f, 100f) // Ensure value stays between 0 and 100
    }

    @Composable
    fun HalfRingDashedProgressBar(
        progress: Float,
        modifier: Modifier = Modifier,
        strokeWidth: Dp = 12.dp,
        dashLength: Float = 20f,
        gapLength: Float = 40f,
        startAngle: Float = -225f,
        totalSweepAngle: Float = 270f,
        textBelow:String = "",
        colorReached: Color = MyColors.colorDarkBlue,
        colorNotReached: Color = MyColors.colorLightDarkBlue.copy(alpha = 0.5f),
        onClick: (() -> Unit)? = null,
        content: @Composable BoxScope.() -> Unit={}
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(100))
                    .clickable(enabled = onClick != null) { onClick?.invoke() }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val diameter = size.minDimension
                    val topLeftOffset = Offset(
                        (size.width - diameter) / 2,
                        (size.height - diameter) / 2
                    )

                    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength, gapLength), 0f)

                    drawArc(
                        color = colorNotReached,
                        startAngle = startAngle,
                        sweepAngle = totalSweepAngle,
                        useCenter = false,
                        topLeft = topLeftOffset,
                        size = Size(diameter, diameter),
                        style = Stroke(
                            width = strokeWidth.toPx(),
                            cap = StrokeCap.Round,
                            pathEffect = dashEffect
                        )
                    )

                   val progressSweepAngle = progress*(totalSweepAngle/100)
                    println(progressSweepAngle)
                    drawArc(
                        color = colorReached,
                        startAngle = startAngle,
                        sweepAngle = progressSweepAngle,
                        useCenter = false,
                        topLeft = topLeftOffset,
                        size = Size(diameter, diameter),
                        style = Stroke(
                            width = strokeWidth.toPx(),
                            cap = StrokeCap.Round,
                            pathEffect = dashEffect
                        )
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize()
                        .clip(RoundedCornerShape(200.dp))
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    content.invoke(this)
                }
            }

            Text(
                text = textBelow.replace(" ", "\n"),
                style = fontParagraphM.copy(fontSize = 13.sp),
                color =  MyColors.colorDarkBlue ,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp)
            )
        }
    }

    @Composable
    private fun DrawTabs(
        interaction: HomeScreenInteraction?,
    ) {
        val tabState = interaction?.getTabIndexState()?.collectAsState()
        TabsRow(
            selectedTab = tabState?.value?.value ?: 0,
            tabs = listOf(
                "Active",
                "Completed"
            ),
            onClick = { index ->
                if (index == 0) {
                    interaction?.onUIEvent(HomePageEvent.LoadCurrent)
                } else if (index == 1) {
                    interaction?.onUIEvent(HomePageEvent.LoadExpired)
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

    interface HomeScreenInteraction{
        fun getUiState(): StateFlow<HomePageUiState?>
        fun onUIEvent(event: HomePageEvent)
        fun getProfileState(): StateFlow<UserProfile?>
        fun getCurrentDate(): String?
        fun getTabIndexState(): StateFlow<ReminderTabType?>
        fun navigate(action:HomeNavigationAction)
        fun getFilteredReminder(): StateFlow<List<ReminderModel>?>
        fun updateReminderStatus(reminderModel: ReminderModel?)

        fun countToday():StateFlow<Int>
        fun countTodayCompleted():StateFlow<Int>
        fun countTotal():StateFlow<Int>
        fun countTotalCompleted():StateFlow<Int>


    }

    enum class HomeNavigationAction{
        ReminderDetail,AddReminder
    }
}