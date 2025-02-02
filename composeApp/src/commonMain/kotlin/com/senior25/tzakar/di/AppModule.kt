package com.senior25.tzakar.di

//import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
//import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreenViewModel
//import com.senior25.tzakar.ui.presentation.screen.registration.forget_password.ForgotPasswordScreenViewModel
//import com.senior25.tzakar.ui.presentation.screen.registration.sign_in.SignInScreenViewModel
//import com.senior25.tzakar.ui.presentation.screen.registration.sign_up.SignUpScreenViewModel
import com.senior25.tzakar.data.repositories.MainRepositoryImpl
import com.senior25.tzakar.data.repositories.RegistrationRepositoryImpl
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.forget_password.ForgotPasswordScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.pincode.PinCodeScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.reset_password.ResetPasswordScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.sign_in.SignInScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.sign_up.SignUpScreenViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single<RegistrationRepository> { RegistrationRepositoryImpl() }
    single<MainRepository> { MainRepositoryImpl() }

    factory { RegistrationScreenViewModel(get()) }
    factory { SignInScreenViewModel(get()) }
    factory { ForgotPasswordScreenViewModel(get()) }
    factory { SignUpScreenViewModel(get()) }
    factory { PinCodeScreenViewModel(get()) }
    factory { ResetPasswordScreenViewModel(get()) }

    factory { MainScreenViewModel(get()) }


}

fun initializeKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule)
    }
}