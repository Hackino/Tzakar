package com.senior25.tzakar.platform_specific.firebase

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.senior25.tzakar.helper.ApplicationProvider

actual fun FirebaseSignOut() {
    FirebaseAuth.getInstance().signOut();
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("218251720662-1nhv6hko4d498otv72l3nvcqp5hcecf4.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val  googleSignInClient:GoogleSignInClient = GoogleSignIn.getClient(ApplicationProvider.application, gso);
    googleSignInClient.signOut().addOnCompleteListener {
        googleSignInClient.revokeAccess().addOnCompleteListener {}
    }
}