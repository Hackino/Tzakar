package com.senior25.tzakar.ui.presentation.screen.main._page

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.avatars.AvatarsModel
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val registrationRepository: RegistrationRepository
): CommonViewModel(){
    var testCount = 0


    init {



    }
}



