package com.senior25.tzakar.di

//
import com.senior25.tzakar.data.repositories.RegistrationRepositoryImpl
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.ui.presentation.screen.registration.forget_password.ForgotPasswordScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.sign_in.SignInScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.sign_up.SignUpScreenViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module


val appModule = module {
    single<RegistrationRepository> { RegistrationRepositoryImpl() }
    viewModel { SignInScreenViewModel(get()) }
    viewModel { ForgotPasswordScreenViewModel(get()) }
    viewModel { SignUpScreenViewModel(get()) }


}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}