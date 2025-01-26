package com.senior25.tzakar.ui.presentation.screen.common

import androidx.core.bundle.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.ui.presentation.graph.NavigationModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class CommonViewModel: ViewModel(){



    private val _navigateTo = MutableStateFlow(NavigationModel())
    val navigateTo = _navigateTo.asStateFlow()
    fun navigateTo(id: Any = 0, bundle: Bundle? = null, title: String? = null) {
        _navigateTo.value = NavigationModel(id, bundle, title)
    }



}



