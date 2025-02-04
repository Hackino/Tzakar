package com.senior25.tzakar.ui.presentation.screen.main.edit_profile

import com.senior25.tzakar.data.local.model.profile.UserProfile

data class ProfilePageData(
    val profileModelInfo: UserProfile? = null,
){
    companion object{
        fun updatePageData(): ProfilePageData {
            return ProfilePageData(
//                profileModelInfo = AccountsPref.myProfileModel,
            )
        }
    }
}

