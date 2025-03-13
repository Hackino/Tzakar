package com.senior25.tzakar.ui.presentation.bottom_sheet.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.senior25.tzakar.data.local.model.menu.MenuModel
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.button.OutlinedCustomButton
import com.senior25.tzakar.ui.presentation.components.menu.MenuCardsGrid
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH3
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesFiltersBottomSheet(
    selectedFilters: MutableList<MenuModel>? = null,
    selectedSorting: MutableList<MenuModel>? = null,
    interaction: CategoriesFiltersSheetInteraction? = null,
    onDismiss:()->Unit = {},
) {
    val skipPartiallyExpanded by rememberSaveable { mutableStateOf(true) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        contentColor = Color.White,
        sheetState = bottomSheetState,
        containerColor = Color.White
    ) {
        val currentSelectedFilters = remember(selectedFilters) { mutableStateListOf<MenuModel>().apply {
            selectedFilters?.let { addAll(it) }
        } }

        val currentSelectedSorting = remember(selectedSorting) { mutableStateListOf<MenuModel>().apply {
            selectedSorting?.let { addAll(it) }
        } }

        val categories = getCategories()
        val sorting = getSortingFilter()

        Column(
            modifier = Modifier.fillMaxWidth()
                .background(Color.White)
                .padding(top = 16.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.filter),
                style = fontH3,
                color =MyColors.colorDarkBlue,
                textAlign = TextAlign.Center
            )

            categories.ifEmpty { null }?.let {
                MenuCardsGrid(cards = it,
                    selected = currentSelectedFilters,
                    onItemClick = {
                        if (currentSelectedFilters.contains(it)) currentSelectedFilters.remove(it)
                        else currentSelectedFilters.add(it)
                        Unit
                    })
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Sorting",
                style = fontH3,
                color =MyColors.colorDarkBlue,
                textAlign = TextAlign.Center
            )

            sorting.ifEmpty { null }?.let {
                MenuCardsGrid(cards = it,
                    selected = currentSelectedSorting,
                    onItemClick = {
                        if (!currentSelectedSorting.contains(it)) {
                            currentSelectedSorting.clear()
                            currentSelectedSorting.add(it)
                        }
                        Unit
                    })
            }

            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = "RESET",
                buttonColor = MyColors.colorPurple,
                onClick = {
                    interaction?.resetPerksFilters()
                    onDismiss()
                }
            )

            OutlinedCustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = "CONFIRM",
                onClick = {
                    interaction?.applyCategoriesFilters(
                        filters = currentSelectedFilters.toList(),
                        sorting = currentSelectedSorting.toList(),
                    )
                    onDismiss()
                }
            )
        }
    }
}

interface CategoriesFiltersSheetInteraction{
    fun applyCategoriesFilters(filters:List<MenuModel>,sorting:List<MenuModel>)
    fun resetPerksFilters()
}