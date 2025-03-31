package com.senior25.tzakar.ui.presentation.screen.main.categories

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


class CategoryViewModel(
    private val maiRepository: MainRepository
) : CommonViewModel(){

    private val _uiState = MutableStateFlow<CategoryPageUiState?>(CategoryPageUiState.Success)
    val uiState: StateFlow<CategoryPageUiState?> get() = _uiState.asStateFlow()

    private val _popUpState = MutableStateFlow<CategoryPagePopUp?>(CategoryPagePopUp.None)
    val popUpState: StateFlow<CategoryPagePopUp?> get() = _popUpState.asStateFlow()

    var title:String? = ""

    var description:String? =""

    private val _reminderDate = MutableStateFlow<String?>(null)
    val reminderDate: StateFlow<String?> get() = _reminderDate.asStateFlow()

    private val _reminderTime = MutableStateFlow<String?>(null)
    val reminderTime: StateFlow<String?> get() = _reminderTime.asStateFlow()

    private val _sound = MutableStateFlow<String?>("sound_1")
    val sound: StateFlow<String?> get() = _sound.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying.asStateFlow()

    private val _longLat = MutableStateFlow<List<String>?>(emptyList())
    val longLat: StateFlow<List<String>?> get() = _longLat.asStateFlow()

    private var player:MediaPlayerHelper? = null

    private val _tabIndexState = MutableStateFlow<CategoryTabType?>(CategoryTabType.TIME)
    val tabIndexState: StateFlow<CategoryTabType?> get() = _tabIndexState.asStateFlow()

    init {
        player = MediaPlayerHelper().apply {
            setOnCompletionListener {
                _isPlaying.value = false
            }
        }
    }

    fun onUIEvent(uiEvent: CategoryPageEvent) = screenModelScope.launch {
        when (uiEvent) {
            CategoryPageEvent.Success -> _uiState.value = CategoryPageUiState.Success
            CategoryPageEvent.LoaderView ->  _uiState.value = CategoryPageUiState.ProgressLoader
            is CategoryPageEvent.UpdatePopUpState -> _popUpState.value = uiEvent.popUp
            is CategoryPageEvent.UpdateTitle -> { title = uiEvent.title }
            is CategoryPageEvent.UpdateDescription -> { description = uiEvent.description }
            is CategoryPageEvent.UpdateReminderDate -> _reminderDate.value = uiEvent.date
            is CategoryPageEvent.UpdateReminderTime -> _reminderTime.value = uiEvent.time
            is CategoryPageEvent.UpdateReminderTone -> {
                _sound.value = uiEvent.tone
                _isPlaying.value = false
                player?.release()
            }
            CategoryPageEvent.UpdatePlayingStatus ->{
                _isPlaying.value = !_isPlaying.value
                if (_isPlaying.value){
                    _sound.value?.let {
                        player?.init(it)
                        player?.play()
                    }
                }else{
                    player?.release()
                }
            }

            CategoryPageEvent.LocationBased -> {
                _tabIndexState.value  = CategoryTabType.LOCATION
            }
            CategoryPageEvent.TimeBased -> {
                _tabIndexState.value  = CategoryTabType.TIME
            }

            is CategoryPageEvent.UpdateLongLat ->{
                _longLat.value = uiEvent.longLat
            }
        }
    }

    override fun onDispose() {
        super.onDispose()
        player?.release()
    }

    fun setCategory(type:Int? = null,onSuccess:()->Unit) {
        screenModelScope.launch {
            _uiState.value = CategoryPageUiState.ProgressLoader
            val parsedDate = _reminderDate.value?.let { LocalDate.parse(it) }
            val parsedTime = _reminderTime.value?.let { LocalTime.parse(it) }
            val reminderDateTime = LocalDateTime(parsedDate!!, parsedTime!!)
            val reminderInstant = reminderDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            val currentTime = LocalDateTime.now().toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

            val reminder = ReminderModel(
                id = generateUUID(),
                type = type,
                title = title,
                description = description,
                date = _reminderDate.value,
                time = _reminderTime.value,
                isEnabled = 1,
                dateTimeEpoch =reminderInstant,
                lastUpdateTimestamp = currentTime,
                sound = _sound.value + ".wav",
                triggerType = _tabIndexState.value?.value
            )
            maiRepository.addReminder(reminder)
            onSuccess()
        }
    }
}

sealed class CategoryPageEvent {
    data object Success: CategoryPageEvent()
    data object LoaderView: CategoryPageEvent()
    data class UpdatePopUpState(val popUp: CategoryPagePopUp) : CategoryPageEvent()
    data class UpdateTitle(val title: String?) : CategoryPageEvent()
    data class UpdateDescription(val description: String?) : CategoryPageEvent()
    data class UpdateReminderDate(val date: String?) : CategoryPageEvent()
    data class UpdateReminderTime(val time: String?) : CategoryPageEvent()
    data class UpdateReminderTone(val tone: String?) : CategoryPageEvent()
    data object UpdatePlayingStatus : CategoryPageEvent()

    data object TimeBased : CategoryPageEvent()
    data object LocationBased : CategoryPageEvent()
    data class UpdateLongLat(val longLat:List<String>) : CategoryPageEvent()

}

sealed class CategoryPagePopUp{
    data object None:CategoryPagePopUp()
    data object SaveChanges:CategoryPagePopUp()
    data object SaveChangesSuccess:CategoryPagePopUp()
    data object SelectAValidDateBefore:CategoryPagePopUp()
}

sealed class CategoryPageUiState {
    data object ProgressLoader : CategoryPageUiState()
    data object Success : CategoryPageUiState()
    data object Error : CategoryPageUiState()
}

enum class CategoryTabType(val value:Int){
    TIME(0),   LOCATION(1)
}