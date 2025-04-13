package com.senior25.tzakar.ui.presentation.screen.main._page

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.senior25.tzakar.data.local.preferences.NotificationStatus
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.di.mainScreenViewModelModule
import com.senior25.tzakar.helper.notification.NotificationHelper
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.platform_specific.exitApp
import com.senior25.tzakar.ui.presentation.app.AppNavigator
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.CategoryType
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.categoriesBottomSheet
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.getCategories
import com.senior25.tzakar.ui.presentation.screen.main.calendar.CalendarTab
import com.senior25.tzakar.ui.presentation.screen.main.categories.CategoryScreen
import com.senior25.tzakar.ui.presentation.screen.main.home.HomeTab
import com.senior25.tzakar.ui.presentation.screen.main.notification_history.NotificationHistoryTab
import com.senior25.tzakar.ui.presentation.screen.main.profile.ProfileTab
import com.senior25.tzakar.ui.theme.MyColors
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

data class MainScreenLauncher(val test:String? = null):Screen {

    @Composable
    override fun Content() {
        var isModuleLoaded by remember { mutableStateOf(false) } // Track module load state
        LaunchedEffect(Unit) {
            loadKoinModules(mainScreenViewModelModule)
            isModuleLoaded = true
        }

        DisposableEffect(Unit) {
            onDispose {
                unloadKoinModules(mainScreenViewModelModule)
                loadKoinModules(mainScreenViewModelModule)
            }
        }

        if (isModuleLoaded) {
            val navigator = LocalNavigator.currentOrThrow
            Navigator(
                screen = MainScreen(),
                onBackPressed = {
                    if (navigator.lastItem::class.simpleName == MainScreenLauncher::class.simpleName) {
                        if (navigator.items.filter { it::class.simpleName == MainScreenLauncher::class.simpleName }.size==1){
                            exitApp()
                        }
                    }
                    true
                },
                key = "MainScreenLauncherNavigator"
            )
        }
    }
}

class MainScreen:Screen {




    override val key: ScreenKey get() = "MainScreenKey"

    @Composable
    override fun Content() {

        val mainViewModel = koinScreenModel<MainScreenViewModel>()

        val popUpState = mainViewModel.popUpState.collectAsState()

        val navigator = LocalNavigator.current

        var shouldRequestPermission by remember { mutableStateOf(false) }

        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    if (SharedPref.notificationPermissionStatus != NotificationStatus.UNKNOWN) {
                        NotificationHelper.isNotificationPermissionGranted { result ->
                            mainViewModel.updateNotificationStatus(result)
                        }
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        SideEffect {
            if (SharedPref.notificationPermissionStatus == NotificationStatus.UNKNOWN)
                shouldRequestPermission = true
        }

        LaunchedEffect(key1 = Unit){
            mainViewModel.init()
        }

        if (shouldRequestPermission) {
            NotificationHelper.requestNotificationPermission { result ->
                print("hackinoooooo request result ${result} \n")
                mainViewModel.updateNotificationStatus(result)
                shouldRequestPermission = false
            }
        }

        TabNavigator(HomeTab,key ="MainScreenTabNavigator"){
            Scaffold(
                content = { CurrentTab() },
                bottomBar = {
                    BottomAppBar(
                        cutoutShape = CircleShape,
                        modifier = Modifier.clip(
                            RoundedCornerShape(
                                topStart = 8.dp,
                                topEnd = 8.dp
                            )
                        ),
                        backgroundColor = MyColors.colorPurple,
                        elevation = 2.dp,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        BottomNavigation(
                            backgroundColor = MyColors.colorPurple,
                        ) {
                            TabNavigationItem(HomeTab) {}
                            TabNavigationItem(CalendarTab) {}
                            BottomNavigationItem(
                                icon = {},
                                label = {   Text(text = "",fontSize = 11.sp) },
                                selected = false,
                                onClick = {  },
                                enabled = false,
                            )
                            TabNavigationItem(NotificationHistoryTab) {}
                            TabNavigationItem(ProfileTab) {}
                        }
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        backgroundColor = MyColors.colorDarkBlue,
                        onClick = {
                            mainViewModel.onUIEvent(MainPageEvent.UpdatePopUpState(MainPagePopUp.CategoriesSheet))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "fab",
                            tint = MyColors.colorWhite
                        )
                    }
                },
                floatingActionButtonPosition = FabPosition.Center,
                isFloatingActionButtonDocked = true,
            )
        }

        if (popUpState.value is MainPagePopUp.CategoriesSheet) {
            categoriesBottomSheet(
                data = getCategories(),
                onItemClick = {
                    navigator?.push(CategoryScreen(CategoryType.getByValue(it?.id)))
                },
                onDismiss = { mainViewModel.onUIEvent(MainPageEvent.UpdatePopUpState(MainPagePopUp.None)) },
            )
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab:Tab,onClick:()->Unit){
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = {
            AppNavigator.popAllTabs()
            tabNavigator.current = tab
            onClick()
        },
        label = {
            val isSelected =  tabNavigator.current == tab
            val color =  if (isSelected) MyColors.colorDarkBlue else MyColors.colorWhite
            Text(text = "" /*tab.options.title*/, color = color, fontSize = 11.sp)
        },
        alwaysShowLabel = true,
        icon = {
            tab.options.icon?.let {painter->
                val isSelected =  tabNavigator.current == tab
                val color =  if (isSelected) MyColors.colorDarkBlue else MyColors.colorWhite
                Icon(painter = painter, contentDescription = tab.options.title, tint = color)
            }
        }
    )
}