package com.senior25.tzakar.helper.authentication.google


sealed interface GoogleAuthResponse {
    data class Success(val account: GoogleAccount) : GoogleAuthResponse
    data class Error(val message: String) : GoogleAuthResponse
    data object Cancelled : GoogleAuthResponse

    suspend fun doOnSuccess(block: suspend (GoogleAccount) -> Unit) {
        when(this) {
            is Success -> block(account)
            Cancelled -> Unit
            is Error -> Unit
        }
    }
}