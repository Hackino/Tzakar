package com.senior25.tzakar.ui.presentation.components.button

import android.app.Activity
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.senior25.tzakar.helper.authentication.google.GoogleAccount
import com.senior25.tzakar.helper.authentication.google.GoogleAuthResponse
import com.senior25.tzakar.helper.authentication.google.GoogleProfile



@Composable
internal actual fun GoogleLoginButton(
    onResponse: (GoogleAuthResponse) -> Unit,
    modifier: Modifier,
    content:@Composable (Modifier,()->Unit) -> Unit
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("218251720662-1nhv6hko4d498otv72l3nvcqp5hcecf4.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val activity = LocalActivity.current as Activity
    val googleSignInClient = GoogleSignIn.getClient(activity, gso)

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(ApiException::class.java)

                onResponse(GoogleAuthResponse.Success(account.googleAccount))
            } catch (e: ApiException) {
                if (result.resultCode == Activity.RESULT_CANCELED) {
                    GoogleAuthResponse.Cancelled
                } else {
                    GoogleAuthResponse.Error(e.fullErrorMessage)
                }.also(onResponse)

                Log.w("TAG", "Google sign in failed", e)
            }
        }
    content(modifier,{ launcher.launch(googleSignInClient.signInIntent) })
}

private val GoogleSignInAccount.googleAccount: GoogleAccount
    get() = GoogleAccount(
        idToken = idToken.orEmpty(),
        accessToken = serverAuthCode.orEmpty(),
        profile = GoogleProfile(
            name = displayName.orEmpty(),
            familyName = familyName.orEmpty(),
            givenName = givenName.orEmpty(),
            email = email.orEmpty(),
            picture = photoUrl?.toString().orEmpty()
        ),
    )

private val ApiException.fullErrorMessage: String
    get() {
        return listOfNotNull(
            "code: $statusCode",
            message?.let { "message: $message" },
            "localizedMessage: $localizedMessage",
            "status: $status",
        ).joinToString("\n")
    }