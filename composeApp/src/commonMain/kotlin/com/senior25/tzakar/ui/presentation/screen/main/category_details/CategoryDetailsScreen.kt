package com.senior25.tzakar.ui.presentation.screen.main.category_details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import com.senior25.tzakar.ktx.ifEmpty
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.platform_specific.map.MapView
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.CategoryType
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.CategoryType.Companion.categoryRes
import com.senior25.tzakar.ui.presentation.components.fields.DateField
import com.senior25.tzakar.ui.presentation.components.fields.TimeField
import com.senior25.tzakar.ui.presentation.components.fields.normalTextField
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar
import com.senior25.tzakar.ui.presentation.screen.main.categories.CategoryTabType
import com.senior25.tzakar.ui.theme.MyColors
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.description
import tzakar_reminder.composeapp.generated.resources.reminder_data
import tzakar_reminder.composeapp.generated.resources.reminder_time
import tzakar_reminder.composeapp.generated.resources.title

data class CategoryDetailsScreen(val reminderId:String? = null): Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<CategoryDetailsScreenViewModel>()

        LaunchedEffect(key1 = Unit){ viewModel.init(reminderId) }

        val interaction = object :CategoryPageInteraction{
            override fun getReminder(): StateFlow<ReminderModel?> = viewModel.reminder
            override fun onBackPress() { navigator.pop() }
        }

        Scaffold(
            topBar = { MyTopAppBar("Details" , showBack = true, interaction = interaction) },
            content = { paddingValues ->  CategoryPageScreen(paddingValues,interaction) },
        )
    }
}

@Composable
private fun CategoryPageScreen(paddingValues: PaddingValues,interaction: CategoryPageInteraction?) {
    val reminderTime = interaction?.getReminder()?.collectAsState()

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
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {

                normalTextField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    label = stringResource(Res.string.title),
                    placeHolder ="",
                    isMandatory = false,
                    value = reminderTime?.value?.title?:"",
                    enabled = false
                )

                reminderTime?.value?.description?.ifEmpty { null }?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    normalTextField(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        label = stringResource(Res.string.description),
                        placeHolder ="",
                        value = it,
                        validate = false,
                        isMandatory = false,
                        enabled = false
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                normalTextField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    label = "Category",
                    value = CategoryType.getByValue(reminderTime?.value?.type).categoryRes()?.let { stringResource(it) },
                    validate = false,
                    placeHolder = "",
                    isMandatory = false,
                    enabled = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (reminderTime?.value?.triggerType == CategoryTabType.TIME.value){

                    DateField(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        label = stringResource(Res.string.reminder_data),
                        placeHolder ="",
                        isMandatory = false,
                        value = reminderTime?.value?.date?:"",
                        validate = true,
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    TimeField(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        label = stringResource(Res.string.reminder_time),
                        placeHolder ="",
                        isMandatory = false,
                        value = reminderTime?.value?.time?:"",
                        validate = true,
                        enabled = false
                    )

                }else if (reminderTime?.value?.triggerType == CategoryTabType.LOCATION.value){
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                            .height(150.dp)
                            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {}
                    ) {
                        val location = listOfNotNull(reminderTime.value?.long, reminderTime.value?.lat).filter { it != 0.0 }
                            .ifEmpty { null }?: emptyList()

                        MapView(
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            cameraLongLat = location,
                            markerLongLat =location,
                            showControls = false,
                            onMarkerSet = {_,_-> }
                        )
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))

                normalTextField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    label = "Tone",
                    value = reminderTime?.value?.sound?.replace(".wav","")?.replace("_", " ")?:"",
                    validate = false,
                    placeHolder = "",
                    isMandatory = false,
                    enabled = false
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


interface CategoryPageInteraction: BackPressInteraction {
    fun getReminder(): StateFlow<ReminderModel?>
}