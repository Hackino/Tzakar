package com.senior25.tzakar.di

import com.senior25.tzakar.data.local.database.dao.NotificationDao
import com.senior25.tzakar.data.local.database.dao.ReminderDao
import com.senior25.tzakar.data.local.database.myDatabase.CreateDatabase
import com.senior25.tzakar.data.local.database.myDatabase.MyDatabase
import com.senior25.tzakar.data.repositories.MainRepositoryImpl
import com.senior25.tzakar.data.repositories.RegistrationRepositoryImpl
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.calendar.CalendarViewModel
import com.senior25.tzakar.ui.presentation.screen.main.categories.CategoryViewModel
import com.senior25.tzakar.ui.presentation.screen.main.category_details.CategoryDetailsScreen
import com.senior25.tzakar.ui.presentation.screen.main.change_password.ChangePasswordScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.EditProfileViewModel
import com.senior25.tzakar.ui.presentation.screen.main.home.HomeScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.notification_history.NotificationHistoryViewModel
import com.senior25.tzakar.ui.presentation.screen.main.profile.ProfileViewModel
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.forget_password.ForgotPasswordScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.pincode.PinCodeScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.reset_password.ResetPasswordScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.sign_in.SignInScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.sign_up.SignUpScreenViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val sharedModule = module {
    single<ReminderDao> { get<MyDatabase>().reminderDao() }
    single<NotificationDao> { get<MyDatabase>().notificationDao() }

    single<RegistrationRepository> { RegistrationRepositoryImpl() }
    single< MainRepository>{MainRepositoryImpl(get(),get())}

    factory { RegistrationScreenViewModel(get()) }
    factory { SignInScreenViewModel(get()) }
    factory { ForgotPasswordScreenViewModel(get()) }
    factory { SignUpScreenViewModel(get()) }
    factory { PinCodeScreenViewModel(get()) }
    factory { ResetPasswordScreenViewModel(get()) }
    factory { CategoryDetailsScreen(get()) }

    factory { MainScreenViewModel(get()) }
    factory { EditProfileViewModel(get()) }
    factory { ProfileViewModel(get()) }
    factory { ChangePasswordScreenViewModel(get()) }

    factory { HomeScreenViewModel(get()) }
    factory { NotificationHistoryViewModel(get()) }

    factory { CategoryViewModel(get()) }
    factory { CalendarViewModel(get()) }
}

fun initializeKoin(config: KoinAppDeclaration? = null,appSpecificModules:KoinApplication.()->Unit = {}) {
    startKoin {
        appSpecificModules.invoke(this)
        config?.invoke(this)
        modules(sharedModule + platformModule()   )
    }
}

val mainScreenViewModelModule = module {
    single { MainScreenViewModel(get()) } // Singleton ViewModel
}

expect fun platformModule(): Module
