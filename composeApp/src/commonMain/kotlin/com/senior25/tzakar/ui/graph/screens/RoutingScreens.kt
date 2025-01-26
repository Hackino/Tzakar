package com.senior25.tzakar.ui.graph.screens

import kotlinx.serialization.Serializable

@Serializable
sealed class RoutingScreens(val route: String){
    @Serializable
    data object Registration: RoutingScreens("Registration")

    @Serializable
    data object Web : RoutingScreens("Web")

    @Serializable
    data object Main: RoutingScreens("Main")
}