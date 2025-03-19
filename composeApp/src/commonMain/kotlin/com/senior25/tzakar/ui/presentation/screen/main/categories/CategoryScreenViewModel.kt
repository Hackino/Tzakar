package com.senior25.tzakar.ui.presentation.screen.main.categories

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.platform_specific.utils.generateUUID
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import dev.gitlive.firebase.auth.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


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


    fun onUIEvent(uiEvent: CategoryPageEvent) = screenModelScope.launch {
        when (uiEvent) {
            CategoryPageEvent.Success -> _uiState.value = CategoryPageUiState.Success
            CategoryPageEvent.LoaderView ->  _uiState.value = CategoryPageUiState.ProgressLoader
            is CategoryPageEvent.UpdatePopUpState -> _popUpState.value = uiEvent.popUp
            is CategoryPageEvent.UpdateTitle -> { title = uiEvent.title }
            is CategoryPageEvent.UpdateDescription -> { description = uiEvent.description }
            is CategoryPageEvent.UpdateReminderDate -> {
                _reminderDate.value = uiEvent.date
            }
            is CategoryPageEvent.UpdateReminderTime -> {
                _reminderTime.value = uiEvent.time

            }
        }
    }

    fun setCategory(type:Int? = null,onSuccess:()->Unit) {
        screenModelScope.launch {
            _uiState.value = CategoryPageUiState.ProgressLoader

            val reminder = ReminderModel(
                id = generateUUID(),
                type = type,
                title = title,
                description = description,
                date = _reminderDate.value,
                time = _reminderTime.value,
                isEnabled = true
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
