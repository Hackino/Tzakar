package com.senior25.tzakar.ui.presentation.screen.main.notifications

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.firebase.FirebaseAuthRsp
import com.senior25.tzakar.data.local.model.firebase.StatusCode
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.ktx.decodeJson
import com.senior25.tzakar.ktx.encodeToJson
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import io.ktor.util.encodeBase64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.incorrect_old_password

class NotificationsScreenViewModel(
    private val mainRepository: MainRepository
): CommonViewModel(){

}

