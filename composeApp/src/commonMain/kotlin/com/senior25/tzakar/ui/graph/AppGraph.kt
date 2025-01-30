package com.senior25.tzakar.ui.graph

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.senior25.tzakar.ui.graph.screens.RegistrationScreens
import com.senior25.tzakar.ui.graph.screens.RoutingScreens
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.forget_password.ForgotPasswordScreen
import com.senior25.tzakar.ui.presentation.screen.registration.sign_in.SignInScreen
import com.senior25.tzakar.ui.presentation.screen.registration.sign_up.SignUpScreen
import com.senior25.tzakar.ui.presentation.screen.web.WebViewScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun AppGraph(startDestination: RoutingScreens, navController: NavHostController) {
    NavHost(navController = navController, startDestination = startDestination.route) {
        registrationGraph(navController)
        composable(
            RoutingScreens.Web.route+"/{title}/{link}",
            arguments = listOf(
                navArgument("title"){
                    type = NavType.StringType
                    nullable = true
                    defaultValue = ""
                },
                navArgument("link"){
                    type = NavType.StringType
                    nullable = true
                    defaultValue = ""
                }
            )
        ) {
            val title = requireNotNull(it.arguments).getString("title")
            val link = requireNotNull(it.arguments).getString("link")
            WebViewScreen(navController,title,link)
        }
    }
}

fun NavGraphBuilder.registrationGraph(
    navController: NavHostController
) {

    navigation(
        startDestination = RegistrationScreens.SignIn.route,
        route = RoutingScreens.Registration.route
    ) {

        composable(RegistrationScreens.SignIn.route) {
            val viewModel: RegistrationScreenViewModel = getViewModel(navController,RoutingScreens.Registration.route)
            SignInScreen(viewModel ,navController)
        }

        composable(RegistrationScreens.SignUp.route) {
            val viewModel: RegistrationScreenViewModel = getViewModel(navController,RoutingScreens.Registration.route)
            SignUpScreen(viewModel,navController)
        }

        composable(RegistrationScreens.Forgot.route) {
            val viewModel: RegistrationScreenViewModel = getViewModel(navController,RoutingScreens.Registration.route)
            ForgotPasswordScreen(viewModel,navController)
        }
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
inline fun <reified T : ViewModel>  getViewModel(navController: NavHostController,route:String):T{
    val backStackEntry = try { navController.getBackStackEntry(route) } catch (e: Exception) { null }
    return backStackEntry?.let {koinViewModel<T>(viewModelStoreOwner = it) } ?: koinViewModel<T>()
}

