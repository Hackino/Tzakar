package com.senior25.tzakar.ui.presentation.components.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.LocalUIViewController
import com.senior25.tzakar.helper.authentication.google.GoogleAccount
import com.senior25.tzakar.helper.authentication.google.GoogleAuthResponse
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSError
import platform.UIKit.UIViewController


import cocoapods.GoogleSignIn.GIDSignIn
import cocoapods.GoogleSignIn.GIDSignInResult
import com.senior25.tzakar.helper.authentication.google.GoogleProfile


@Composable
internal actual fun GoogleLoginButton(
    onResponse: (GoogleAuthResponse) -> Unit,
    modifier: Modifier,
    content:@Composable (Modifier,()->Unit) -> Unit
) {
    val uiViewController = LocalUIViewController.current
    content(modifier,{ googleLogin(uiViewController, onResponse) })
}

@OptIn(ExperimentalForeignApi::class)
private fun googleLogin(
    uiViewController: UIViewController,
    onLoggedIn: (GoogleAuthResponse) -> Unit
) {
    GIDSignIn.sharedInstance.signInWithPresentingViewController(uiViewController) { result, error ->
        when {
            result != null -> onLoggedIn(GoogleAuthResponse.Success(result.toGoogleAccount))
            error != null -> onLoggedIn(GoogleAuthResponse.Error(error.fullErrorMessage))
            else -> onLoggedIn(GoogleAuthResponse.Cancelled)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private val GIDSignInResult.toGoogleAccount: GoogleAccount
    get() = GoogleAccount(
        idToken = user.idToken?.tokenString.orEmpty(),
        accessToken = user.accessToken.tokenString,
        profile = GoogleProfile(
            name = user.profile?.name.orEmpty(),
            familyName = user.profile?.familyName.orEmpty(),
            givenName = user.profile?.givenName.orEmpty(),
            email = user.profile?.email.orEmpty(),
            picture = user.profile?.imageURLWithDimension(100u)?.absoluteString
        ),
    )

private val NSError.fullErrorMessage: String
    get() {
        val underlyingErrors = underlyingErrors.joinToString(", ") { it.toString() }
        val recoveryOptions = localizedRecoveryOptions?.joinToString(", ") { it.toString() }

        return listOfNotNull(
            "code: $code",
            domain?.let { "domain: $domain" },
            "description: $localizedDescription",
            localizedFailureReason?.let { "reason: $localizedFailureReason" },
            localizedRecoverySuggestion?.let { "suggestion: $localizedRecoverySuggestion" },
            "underlyingErrors: $underlyingErrors",
            "recoveryOptions: $recoveryOptions".takeIf { recoveryOptions != null },
        ).joinToString("\n")
    }