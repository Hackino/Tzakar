package com.senior25.tzakar.ui.presentation.screen.main.full_screen_map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.platform_specific.map.MapView
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.CategoryType
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.CategoryType.Companion.categoryHeaderRes
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.theme.MyColors
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.set_reminder

data class FullScreenMap(val list: List<String>?): Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow


        val interaction = object :FullScreenMapInteraction{
            override fun onContinueClick() {
            }

            override fun onBackPress() { navigator.pop() }
        }

        Scaffold(
            topBar = { MyTopAppBar("Reminder Location" , showBack = true, interaction = interaction) },
            content = {paddingValues ->  FullScreenMap(interaction) },
        )
    }
}

@Composable
private fun FullScreenMap(interaction: FullScreenMapInteraction?) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
        shape =  RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MyColors.colorLightGrey),
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier.background(MyColors.colorOffWhite)) {
            MapView(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                longLat = listOf(35.5018,33.8938)
            )
            CustomButton(
                isEnabled = true,
                modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth().padding(horizontal = 16.dp).align(Alignment.BottomCenter),
//                onClick = { interaction?.onUIEvent(CategoryPageEvent.UpdatePopUpState(CategoryPagePopUp.SaveChanges)) },
                text = "Set Location"
            )
        }

    }
}


interface FullScreenMapInteraction: BackPressInteraction {
    fun onContinueClick(){}
}