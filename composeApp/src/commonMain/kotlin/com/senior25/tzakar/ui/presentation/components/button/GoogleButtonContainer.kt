package com.senior25.tzakar.ui.presentation.components.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.senior25.tzakar.helper.authentication.google.GoogleAuthResponse
import com.senior25.tzakar.ktx.ifEmpty
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
internal expect fun GoogleLoginButton(
    onResponse: (GoogleAuthResponse) -> Unit,
    modifier: Modifier = Modifier,
    content:@Composable (Modifier,()->Unit) -> Unit
)

@Composable
fun GoogleButtonUiContainer(
    onResponse: (GoogleAuthResponse) -> Unit,
    modifier: Modifier = Modifier,
    content:@Composable (Modifier,()->Unit) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    GoogleLoginButton(
        modifier = modifier,
        onResponse = { response ->
            coroutineScope.launch(Dispatchers.IO) {
                response.doOnSuccess { account ->
                    Firebase.auth.signInWithCredential(

                        GoogleAuthProvider.credential(account.idToken, account.accessToken.ifEmpty { null })
                    )
                }

                withContext(Dispatchers.Main) { onResponse(response) }
            }
        },
        content =content
    )
}