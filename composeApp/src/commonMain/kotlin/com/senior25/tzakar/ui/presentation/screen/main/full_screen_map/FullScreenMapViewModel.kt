package com.senior25.tzakar.ui.presentation.screen.main.full_screen_map

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.helper.media.MediaPlayerHelper
import com.senior25.tzakar.platform_specific.utils.generateUUID
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import com.senior25.tzakar.ui.presentation.screen.main.home.HomePageEvent
import com.senior25.tzakar.ui.presentation.screen.main.home.ReminderTabType
import dev.gitlive.firebase.auth.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import network.chaintech.kmp_date_time_picker.utils.now


class FullScreenMapViewModel(
    private val maiRepository: MainRepository
) : CommonViewModel(){

    private val _longLat = MutableStateFlow<List<Double>?>(emptyList())
    val longLat: StateFlow<List<Double>?> get() = _longLat.asStateFlow()

    fun onUIEvent(uiEvent: FullScreenMapPageEvent) = screenModelScope.launch {
        when (uiEvent) {
            is FullScreenMapPageEvent.UpdateLongLat ->{
                _longLat.value = uiEvent.longLat
            }
        }
    }
}

sealed class FullScreenMapPageEvent {
    data class UpdateLongLat(val longLat:List<Double>) : FullScreenMapPageEvent()
}

sealed class FullScreenMapPageUiState {
    data object ProgressLoader : FullScreenMapPageUiState()
    data object Success : FullScreenMapPageUiState()
    data object Error : FullScreenMapPageUiState()
}