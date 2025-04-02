package com.senior25.tzakar.ui.presentation.screen.main.full_screen_map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.platform_specific.map.MapView
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar
import com.senior25.tzakar.ui.presentation.screen.main._page.MainPageEvent
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.theme.MyColors
import kotlinx.coroutines.flow.StateFlow

data class FullScreenMap(val list: List<Double>?): Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val mainViewModel = koinScreenModel<MainScreenViewModel>()

        val screenModel = koinScreenModel<FullScreenMapViewModel>()

        LaunchedEffect(key1 = Unit){
            if (screenModel.longLat.value?.ifEmpty { null } == null ){
                screenModel.onUIEvent(FullScreenMapPageEvent.UpdateLongLat(list?: emptyList()))
            }
        }

        val interaction = object :FullScreenMapInteraction{
            override fun onContinueClick() {
                mainViewModel.onUIEvent(MainPageEvent.UpdateLongLat(screenModel.longLat.value?: emptyList()))
                navigator.pop()
            }

            override fun onUIEvent(event: FullScreenMapPageEvent) {
                screenModel.onUIEvent(event)
            }

            override fun getLongLat()  = screenModel.longLat

            override fun onBackPress() {
                navigator.pop()
            }
        }

        Scaffold(
            topBar = { MyTopAppBar("Reminder Location" , showBack = true, interaction = interaction) },
            content = {paddingValues ->  FullScreenMap(interaction) },
        )
    }
}

@Composable
private fun FullScreenMap(interaction: FullScreenMapInteraction?) {

    val getLongLat = interaction?.getLongLat()?.collectAsState()

    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
        shape =  RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MyColors.colorLightGrey),
        elevation = 4.dp
    ) {

        Box(modifier = Modifier.background(MyColors.colorOffWhite)) {

            MapView(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                cameraLongLat =getLongLat?.value?.ifEmpty { null }?: listOf(35.5018,33.8938),
                markerLongLat = getLongLat?.value?.ifEmpty { null },
                showControls = true,
                onMarkerSet = {long,lat->
                    interaction?.onUIEvent(FullScreenMapPageEvent.UpdateLongLat(listOf(long,lat)))
                }
            )

            CustomButton(
                isEnabled = getLongLat?.value?.ifEmpty { null } != null,
                modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth().padding(horizontal = 16.dp).align(Alignment.BottomCenter),
                onClick = { interaction?.onContinueClick() },
                text = "Set Location"
            )
        }
    }
}

interface FullScreenMapInteraction: BackPressInteraction {
    fun onContinueClick(){}
    fun onUIEvent(event: FullScreenMapPageEvent){}
    fun getLongLat(): StateFlow<List<Double>?>
}