package com.senior25.tzakar.helper.authentication.email

import com.senior25.tzakar.data.local.model.firebase.FirebaseAuthRsp
import com.senior25.tzakar.data.local.model.firebase.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthService {

    val currentUserId: String

    val isAuthenticated: Boolean

    val currentUser: Flow<FirebaseUser>

    suspend fun authenticate(email: String, password: String): FirebaseAuthRsp

    suspend fun createUser(email: String, password: String): FirebaseAuthRsp

    suspend fun signOut()
}