package com.senior25.tzakar.ui.presentation.screen.registration.pincode

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PinCodeScreenViewModel(
    private val registrationRepository: RegistrationRepository
): CommonViewModel() {

    private val _uiState = MutableStateFlow<PinCodeScreenPageUiState?>(PinCodeScreenPageUiState.Success)
    val uiState: StateFlow<PinCodeScreenPageUiState?> get() = _uiState.asStateFlow()

    var pinCodeMap: String? = null

    fun onUIEvent(uiEvent: PinCodeScreenPageEvent) = screenModelScope.launch {
        when (uiEvent) {
            is PinCodeScreenPageEvent.UpdatePinCodeMap -> pinCodeMap = uiEvent.pincode
            PinCodeScreenPageEvent.Success -> _uiState.value = PinCodeScreenPageUiState.Success
            PinCodeScreenPageEvent.LoaderView -> _uiState.value = PinCodeScreenPageUiState.ProgressLoader
        }
    }
}

sealed class PinCodeScreenPageEvent {
    data class UpdatePinCodeMap(var pincode:String):PinCodeScreenPageEvent()
    data object Success:PinCodeScreenPageEvent()
    data object LoaderView:PinCodeScreenPageEvent()
}

sealed class PinCodeScreenPageUiState {
    data object ProgressLoader : PinCodeScreenPageUiState()
    data object Success : PinCodeScreenPageUiState()
    data object Error : PinCodeScreenPageUiState()
}