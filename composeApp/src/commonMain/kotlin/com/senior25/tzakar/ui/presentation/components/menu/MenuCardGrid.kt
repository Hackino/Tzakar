package com.senior25.tzakar.ui.presentation.components.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.senior25.tzakar.data.local.model.menu.MenuModel

@Composable
fun MenuCardsGrid(
    cards:List<MenuModel>? = null,
    selected:List<MenuModel>? = null,
    onItemClick:(MenuModel)->Unit? = {}
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        if (cards?.isNotEmpty() == true) {

            val rows:List<List<MenuModel>>? =  cards.chunked(2)

            rows?.ifEmpty { null }?.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    when (rowItems.size) {
                        1 -> {
                            MenuCardItem(
                                modifier = Modifier.weight(1f)
                                    .aspectRatio(361f / 106f),
                                card = rowItems.first(),
                                selected= selected?.contains(rowItems.first()),
                                onClick = {
                                    onItemClick(rowItems.first())
                                }
                            )
                        }
                        2 -> {
                            rowItems.forEach { item ->
                                MenuCardItem(
                                    modifier = Modifier.weight(1f).aspectRatio(182f / 106f),
                                    selected= selected?.contains(item),
                                    card =  item,
                                    onClick = { onItemClick(item)}
                                )
                            }
                        }

                        else -> {
                            rowItems.forEach { item ->
                                MenuCardItem(
                                    modifier = Modifier.weight(1f).aspectRatio(115f / 106f),
                                    card =  item,
                                    selected= selected?.contains(item),
                                    onClick = { onItemClick( item) }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}