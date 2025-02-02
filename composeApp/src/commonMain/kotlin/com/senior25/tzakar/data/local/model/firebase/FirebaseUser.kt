package com.senior25.tzakar.data.local.model.firebase

data class FirebaseUser(
    val id: String? = null,
    val isAnonymous: Boolean? = null,
    val googleToken:String? = null,
    val userName:String? = null,
    val email:String? = null,
    val password:String? = null
)