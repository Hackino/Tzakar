package com.senior25.tzakar.ui.presentation.screen.main.home

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val mainRepository: MainRepository
): CommonViewModel(){

    private val _uiState = MutableStateFlow<HomePageUiState?>(null)
    val uiState: StateFlow<HomePageUiState?> get() = _uiState.asStateFlow()

    private val _popUpState = MutableStateFlow<HomePagePopUp?>(HomePagePopUp.None)
    val popUpState: StateFlow<HomePagePopUp?> get() = _popUpState.asStateFlow()

    private var _homePageData = MutableStateFlow<HomePageData?>(HomePageData())

    fun onUIEvent(uiEvent: HomePageEvent) = screenModelScope.launch {
        when (uiEvent) {
            HomePageEvent.Success -> _uiState.value = HomePageUiState.Success(_homePageData.value)
            HomePageEvent.LoaderView ->  _uiState.value = HomePageUiState.ProgressLoader(_homePageData.value)
            is HomePageEvent.UpdatePopUpState -> _popUpState.value = uiEvent.popUp
        }
    }

    private fun updateState(newState: HomePageUiState) {
        if (_uiState.value != newState) {
            _homePageData.value =newState.data
            _uiState.value = newState
        }
    }
}

sealed class HomePageEvent {
    data object Success: HomePageEvent()
    data object LoaderView: HomePageEvent()
    data class UpdatePopUpState(val popUp: HomePagePopUp) : HomePageEvent()
}

sealed class HomePageUiState(open val data: HomePageData?) {
    data class Loading(override val data: HomePageData?) : HomePageUiState(data)
    data class Success(override val data: HomePageData?) : HomePageUiState(data)
    data class ProgressLoader(override val data: HomePageData?) : HomePageUiState(data)
    data class Error(override val data: HomePageData?) : HomePageUiState(data)
}

sealed class HomePagePopUp{
    data object None:HomePagePopUp()
}