package com.senior25.tzakar.ui.presentation.screen.main.notification_history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.platform_specific.ui.getScreenWidth
import com.senior25.tzakar.ui.presentation.app.AppNavigator
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar
import com.senior25.tzakar.ui.presentation.screen.common.composable.no_data.NoDataWidget
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.notification_history.NotificationHistoryPageData.Companion.checkIfNotContainData
import com.senior25.tzakar.ui.presentation.screen.main.notification_history.composable.ListShimmer
import com.senior25.tzakar.ui.presentation.screen.main.notification_history.composable.NotificationCardWidget
import com.senior25.tzakar.ui.presentation.screen.main.notification_history.composable.NotificationItemInteraction
import com.senior25.tzakar.ui.theme.MyColors
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.notification_history

object NotificationHistoryTab: Tab {

    override val key: ScreenKey get() = "NotificationHistoryTab"

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Notifications)
            val title  = "Notification history"
            val index:UShort = 1u
            return TabOptions(icon = icon,title = title, index = index)
        }

    @Composable
    override fun Content() {
        Navigator(NotificationHistoryScreen(), key = "NotificationHistoryTabNavigator"){ navigator->
            SlideTransition(navigator)
        }
    }
}

class NotificationHistoryScreen: Screen {

    override val key: ScreenKey get() = "NotificationHistoryPage"

    @Composable
    override fun Content() {
        AppNavigator.addTabNavigator(LocalNavigator.current)
        val navigator = LocalNavigator.current
        val mainViewModel = koinScreenModel<MainScreenViewModel>()
        val viewModel = koinScreenModel<NotificationHistoryViewModel>()

        LaunchedEffect(key1 = Unit) { viewModel.init() }

        val interaction  = object : NotificationHistoryPageScreenInteraction {

            override fun getUiState(): StateFlow<NotificationHistoryPageUiState?> = viewModel.uiState

            override fun onUIEvent(event: NotificationHistoryPageEvent) { viewModel.onUIEvent(event) }

            override fun onNotificationClick(data: NotificationModel?) {}
        }
        Scaffold(
            backgroundColor = MyColors.colorOffWhite,
            topBar = { MyTopAppBar( stringResource(Res.string.notification_history), showBack = false, centerTitle = false) },
            content = { NotificationPageScreen(interaction) }
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotificationPageScreen(
    interaction: NotificationHistoryPageScreenInteraction?,
){
    val uiState: State<NotificationHistoryPageUiState?>? = interaction?.getUiState()?.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState?.value is NotificationHistoryPageUiState.Refreshing,
        onRefresh = {
            when(uiState?.value){
                is NotificationHistoryPageUiState.Refreshing->{}
                else-> interaction?.onUIEvent(NotificationHistoryPageEvent.Refresh)
            }
        }
    )

    val screenWidthDp = getScreenWidth()
    val lazyState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background( MyColors.colorOffWhite)
    ) {
        Box(
            modifier = Modifier
                .pullRefresh(pullRefreshState)
                .fillMaxSize()
        ) {

            LazyColumn(
                state = lazyState,
                modifier = Modifier
                    .matchParentSize()
                    .background(MyColors.colorLightGrey)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
            ) {

                if (uiState?.value is NotificationHistoryPageUiState.Loading || uiState?.value is NotificationHistoryPageUiState.Refreshing) {
                    item { Spacer(modifier = Modifier.height(8.dp));ListShimmer() }
                } else {
                    uiState?.value?.data?.notifications?.ifEmpty { null }?.let { data ->
                        itemsIndexed(items = data) { index, item ->
                            val border = when (index) {
                                0 -> RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp)
                                data.size - 1 -> RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)
                                else -> RectangleShape
                            }

                            NotificationCardWidget(
                                modifier = Modifier.width(screenWidthDp),
                                item = item,
                                border = border,
                                addSpacer = index != data.size - 1,
                                interaction = interaction
                            )
                        }
                    }
                }
            }

            if (uiState?.value is NotificationHistoryPageUiState.Error) {
                if (uiState.value?.data?.checkIfNotContainData() == true) NoDataWidget(modifier = Modifier.fillMaxSize(),)
            }
            if (uiState?.value is NotificationHistoryPageUiState.NoData) NoDataWidget(modifier = Modifier.fillMaxSize())
            PullRefreshIndicator(
                uiState?.value is NotificationHistoryPageUiState.Refreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

interface NotificationHistoryPageScreenInteraction: NotificationItemInteraction {
    fun getUiState(): StateFlow<NotificationHistoryPageUiState?>
    fun onUIEvent(event: NotificationHistoryPageEvent)
}
