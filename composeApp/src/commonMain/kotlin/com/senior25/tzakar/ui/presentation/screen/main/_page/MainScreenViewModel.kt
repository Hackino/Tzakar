package com.senior25.tzakar.ui.presentation.screen.main._page

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val registrationRepository: RegistrationRepository
): CommonViewModel(){

    private val _popUpState = MutableStateFlow<MainPagePopUp?>(MainPagePopUp.None)
    val popUpState: StateFlow<MainPagePopUp?> get() = _popUpState.asStateFlow()


fun onUIEvent(uiEvent: MainPageEvent) =
    screenModelScope.launch {
        when (uiEvent) {
            is MainPageEvent.UpdatePopUpState -> _popUpState.value = uiEvent.popUp
        }
    }

}

sealed class MainPageEvent {
    data class UpdatePopUpState(val popUp: MainPagePopUp) : MainPageEvent()
}


sealed class MainPagePopUp{
    data object None:MainPagePopUp()
    data object LoginPopUp:MainPagePopUp()
    data object CategoriesSheet:MainPagePopUp()

}



