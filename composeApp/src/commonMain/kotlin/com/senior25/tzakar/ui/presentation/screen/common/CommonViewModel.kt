package com.senior25.tzakar.ui.presentation.screen.common

import androidx.core.bundle.Bundle
import androidx.lifecycle.ViewModel
import com.senior25.tzakar.ui.graph.NavigationModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class CommonViewModel: ViewModel(){

    private val _navigateTo = MutableStateFlow(NavigationModel())
    val navigateTo = _navigateTo.asStateFlow()
    fun navigateTo(id: Any = 0, bundle: Bundle? = null, title: String? = null) {
        _navigateTo.value = NavigationModel(id, bundle, title)
    }
}



