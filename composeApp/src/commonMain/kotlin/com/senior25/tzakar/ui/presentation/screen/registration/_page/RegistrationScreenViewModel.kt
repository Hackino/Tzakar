package com.senior25.tzakar.ui.presentation.screen.registration._page

import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
class RegistrationScreenViewModel(
    private val registrationRepository: RegistrationRepository
): CommonViewModel(){


    var registrationData = RegistrationData()
}



data class RegistrationData(
    val pinCode:String? = null,
    val email:String? = null,
    val password:String? = null,
)