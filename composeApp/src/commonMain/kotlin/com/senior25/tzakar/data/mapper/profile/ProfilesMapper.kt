package com.senior25.tzakar.data.mapper.profile

import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.helper.authentication.google.GoogleAccount

fun GoogleAccount.toUserProfileMapper() =
    UserProfile(
        googleToken = idToken,
        email = profile.email
    )


