package com.senior25.tzakar.data.local.model.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserProfile(
    @SerialName("id")
    val id: String? = null,
    @SerialName("googleToken")
    val googleToken:String? = null,
    @SerialName("userName")
    val userName:String? = null,
    @SerialName("email")
    val email:String? = null,
    @SerialName("password")
    val password:String? = null
)