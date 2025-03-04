package com.senior25.tzakar.data.local.model.gender

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenderModel(
    @SerialName("id")
    val id:Int? = null,
    @SerialName("value")
    val value:String? = null
)

object Gender{
    fun getGenders()= listOf(
        GenderModel(
            id =1,
            value  = "Male"
        ),
        GenderModel(
            id =2,
            value  = "Female"
        ),
        GenderModel(
            id =3,
            value  = "Non-Binary"
        )
    )
}

enum class Genders(val value:Int){
    UNKNOWN(-1),MALE(1),FEMALE(2),NON_BINARY(3);

    companion object {
        private val VALUES = Genders.values()
        fun getByValue(value: Int?) = VALUES.firstOrNull { it.value == value } ?: UNKNOWN
    }
}
