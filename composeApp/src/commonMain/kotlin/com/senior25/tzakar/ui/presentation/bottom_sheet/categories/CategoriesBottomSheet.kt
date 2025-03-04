package com.senior25.tzakar.ui.presentation.bottom_sheet.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.senior25.tzakar.data.local.model.menu.MenuModel
import com.senior25.tzakar.ui.presentation.components.menu.MenuCardsGrid
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontLink
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.app_icon
import tzakar_reminder.composeapp.generated.resources.choose_a_category


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun categoriesBottomSheet(
    data:List<MenuModel>? = null,
    onDismiss:()->Unit = {},
    onItemClick: (MenuModel?) -> Unit
) {
    val skipPartiallyExpanded by rememberSaveable { mutableStateOf(true) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        contentColor = Color.White,
        sheetState = bottomSheetState,
        containerColor = Color.White
    ) {
        categoriesBottomSheet(
            data =data,
            onItemClick  = {
                onDismiss()
                onItemClick.invoke(it)
            },
        )
    }
}

@Composable
private fun categoriesBottomSheet(
    data:List<MenuModel>? = null,
    onItemClick:(MenuModel?)->Unit = {}
) {
    Column(
        modifier = Modifier
            .background(
                color = MyColors.colorWhite,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            ).padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(Res.string.choose_a_category),
            color =MyColors.colorDarkBlue,
            style = fontLink,
            modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        MenuCardsGrid(cards = data,onItemClick = onItemClick)
    }
}

fun getCategories() = listOf(
    MenuModel(
        iconRes = Res.drawable.app_icon,
        title = "Groceries"
    ),
    MenuModel(
        iconRes = Res.drawable.app_icon,
        title = "Movies + TV"
    ),
    MenuModel(
        iconRes = Res.drawable.app_icon,
        title = "Games"
    ),
    MenuModel(
        iconRes = Res.drawable.app_icon,
        title = "Birthdays"
    ),
    MenuModel(
        iconRes = Res.drawable.app_icon,
        title = "Bills"
    ),

    MenuModel(
        iconRes = Res.drawable.app_icon,
        title = "Medications"
    ),

    MenuModel(
        iconRes = Res.drawable.app_icon,
        title = "Custom"
    ),
)
