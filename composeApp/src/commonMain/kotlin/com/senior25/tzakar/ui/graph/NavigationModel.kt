package com.senior25.tzakar.ui.graph

import androidx.core.bundle.Bundle

data class NavigationModel(
    val navId: Any=0,
    val bundle: Bundle?=null,
    val title:String? = null,
    val popInclusive: Boolean = false
)
