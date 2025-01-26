package com.senior25.tzakar.data.local.model

import dev.gitlive.firebase.auth.AuthResult

data class FirebaseAuthRsp(
    val authResult: AuthResult?= null,
    val statusCode: StatusCode?= null,
)

data class StatusCode(
    val code: Int?= null,
    val errorMessage: String?= null,
)

