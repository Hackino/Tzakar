package com.senior25.tzakar.helper.authentication.google

data class GoogleAccount(
    val idToken: String,
    val accessToken: String,
    val profile: GoogleProfile
)

data class GoogleProfile(
    val name: String,
    val familyName: String,
    val givenName: String,
    val email: String,
    val picture: String?
)