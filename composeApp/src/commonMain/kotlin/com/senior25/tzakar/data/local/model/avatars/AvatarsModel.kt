package com.senior25.tzakar.data.local.model.avatars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AvatarsModel (
    @SerialName("male_avatars")
    val maleAvatars:List<String>? = null,
    @SerialName("female_avatars")
    val femaleAvatars:List<String>? = null
)