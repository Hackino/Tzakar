package com.senior25.tzakar.ui.presentation.screen.main._page

import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel

class MainScreenViewModel(
    private val registrationRepository: RegistrationRepository
): CommonViewModel(){
    var testCount = 0
}



