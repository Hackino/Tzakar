package com.senior25.tzakar.data.local.model.menu

import org.jetbrains.compose.resources.DrawableResource

data class MenuModel(
    val id:String? = null,
    val icon:String? = null,
    val iconRes:DrawableResource? = null,
    val title:String? = null,
)