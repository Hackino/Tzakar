package com.senior25.tzakar.ui.presentation.screen.common

import androidx.core.bundle.Bundle
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.firebase.StatusCode
import com.senior25.tzakar.ui.graph.NavigationModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class CommonViewModel: ScreenModel{

    internal val _errorStatusCode = MutableStateFlow<StatusCode?>(null)
    val errorStatusCode: StateFlow<StatusCode?> get() = _errorStatusCode.asStateFlow()

    internal val _isLoading = MutableStateFlow<Boolean?>(null)
    val isLoading: StateFlow<Boolean?> get() = _isLoading.asStateFlow()

    private val _navigateTo = MutableStateFlow(NavigationModel())
    val navigateTo = _navigateTo.asStateFlow()
    fun navigateTo(id: Any = 0, bundle: Bundle? = null, title: String? = null) {
        _navigateTo.value = NavigationModel(id, bundle, title)
    }


    val coroutineContext = SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
        println("BaseViewModel: Error: ${throwable.message}")
        //show error message using snackbar for all errors
    }

    private var job: Job? =  null

    fun launchWithCatchingException(block: suspend CoroutineScope.() -> Unit) {
        job = screenModelScope.launch(
            context = coroutineContext,
            block = block
        )
    }


    override fun onDispose() {
        super.onDispose()
        job?.cancel()
    }
}



