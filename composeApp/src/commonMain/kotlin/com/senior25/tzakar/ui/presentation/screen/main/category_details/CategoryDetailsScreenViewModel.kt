package com.senior25.tzakar.ui.presentation.screen.main.category_details

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CategoryDetailsScreenViewModel(
    private val maiRepository: MainRepository
) : CommonViewModel(){

    private val _reminder = MutableStateFlow<ReminderModel?>(null)
    val reminder: StateFlow<ReminderModel?> get() = _reminder.asStateFlow()

    fun init(reminderId:String?){
        screenModelScope.launch(Dispatchers.IO) {
            maiRepository.getReminderById(reminderId).collectLatest {
                _reminder.value = it
            }
        }
    }
}