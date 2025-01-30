package com.senior25.tzakar.ui.graph.screens

@kotlinx.serialization.Serializable
sealed class RegistrationScreens(val route: String){

    @kotlinx.serialization.Serializable
    data object Parent: RegistrationScreens("RegistrationParent")

    @kotlinx.serialization.Serializable
    data object SignIn : RegistrationScreens("SignIn")

    @kotlinx.serialization.Serializable
    data object SignUp: RegistrationScreens("SignUp")

    @kotlinx.serialization.Serializable
    data object Forgot: RegistrationScreens("Forgot")

}