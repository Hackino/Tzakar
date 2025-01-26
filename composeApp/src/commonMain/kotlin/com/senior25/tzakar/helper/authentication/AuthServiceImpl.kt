package com.senior25.tzakar.helper.authentication

import com.senior25.tzakar.data.local.model.FirebaseAuthRsp
import com.senior25.tzakar.data.local.model.StatusCode
import com.senior25.tzakar.data.local.model.User
import dev.gitlive.firebase.FirebaseTooManyRequestsException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthActionCodeException
import dev.gitlive.firebase.auth.FirebaseAuthEmailException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.FirebaseAuthMultiFactorException
import dev.gitlive.firebase.auth.FirebaseAuthRecentLoginRequiredException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import dev.gitlive.firebase.auth.FirebaseAuthWeakPasswordException
import dev.gitlive.firebase.auth.FirebaseAuthWebException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthServiceImpl(
    val auth: FirebaseAuth,
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) : AuthService {

    override val currentUserId: String get() = auth.currentUser?.uid.toString()

    override val isAuthenticated: Boolean get() = auth.currentUser != null && auth.currentUser?.isAnonymous == false

    override val currentUser: Flow<User> =
        auth.authStateChanged.map { it?.let { User(it.uid, it.isAnonymous) } ?: User() }

    private suspend fun  <T> launchWithAwait(block : suspend  () -> T): T {
        return block()

    }
    override suspend fun authenticate(email: String, password: String): FirebaseAuthRsp {
        return  launchWithAwait {
            try {
                FirebaseAuthRsp(auth.signInWithEmailAndPassword(email, password))
            }catch (e: Exception){
                FirebaseAuthRsp(statusCode = getException(e))
            }
        }
    }

    suspend fun sendResetPassword(email: String){
        return launchWithAwait {
            auth.sendPasswordResetEmail(email)
        }
    }

    override suspend fun createUser(email: String, password: String):FirebaseAuthRsp {
        return launchWithAwait {
            try {
                FirebaseAuthRsp(auth.createUserWithEmailAndPassword(email, password))
            }catch (e: Exception){
                FirebaseAuthRsp(statusCode = getException(e))
            }
        }
    }

    override suspend fun signOut() {
        if (auth.currentUser?.isAnonymous == true) {
            auth.currentUser?.delete()
        }
        auth.signOut()
    }

    private fun getException(e:Exception):StatusCode{
        val code= when(e){
            is FirebaseAuthActionCodeException ->1
            is FirebaseAuthEmailException ->2
            is FirebaseAuthWeakPasswordException ->3
            is FirebaseAuthInvalidCredentialsException ->4
            is FirebaseAuthInvalidUserException ->5
            is FirebaseAuthMultiFactorException ->6
            is FirebaseAuthRecentLoginRequiredException ->7
            is FirebaseAuthUserCollisionException ->8
            is FirebaseAuthWebException ->-9
            is FirebaseTooManyRequestsException ->-10

            else-> -1
        }
        return StatusCode(code = code)
    }

    enum class FirebaseException(val value:Int){
        Unknown(-1),
        FirebaseAuthActionCodeException(1),
        FirebaseAuthEmailException(2),
        FirebaseAuthWeakPasswordException(3),
        FirebaseAuthInvalidCredentialsException(4),
        FirebaseAuthInvalidUserException(5),
        FirebaseAuthMultiFactorException(6),
        FirebaseAuthRecentLoginRequiredException(7),
        FirebaseAuthUserCollisionException(8),
        FirebaseAuthWebException(9),
        FirebaseTooManyRequestsException(10);
        companion object {
            private val VALUES = FirebaseException.values()
            fun getByValue(value: Int?) = VALUES.firstOrNull { it.value == value }?:Unknown
        }
    }


}