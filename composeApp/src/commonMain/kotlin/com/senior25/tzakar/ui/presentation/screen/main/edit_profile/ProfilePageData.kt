package com.senior25.tzakar.ui.presentation.screen.main.edit_profile

import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.data.local.preferences.SharedPref

data class ProfilePageData(
    val profileModelInfo: UserProfile? = SharedPref.loggedInProfile,
){
    companion object{

        fun ProfilePageData.updatePageData(profile:UserProfile?): ProfilePageData {
            return this.copy(
                profileModelInfo = profile?:SharedPref.loggedInProfile,
            )
        }

    }
}

