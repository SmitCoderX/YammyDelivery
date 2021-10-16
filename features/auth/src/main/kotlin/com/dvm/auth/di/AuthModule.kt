package com.dvm.auth

import com.dvm.auth.login.LoginViewModel
import com.dvm.auth.register.RegisterViewModel
import com.dvm.auth.restore.PasswordRestoreViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {

    viewModel {
        LoginViewModel(
            authApi = get(),
            profileRepository = get(),
            datastore = get(),
            updateService = get(),
            navigator = get(),
            savedState = get()
        )
    }

    viewModel {
        RegisterViewModel(
            authApi = get(),
            datastore = get(),
            profileRepository = get(),
            updateService = get(),
            navigator = get(),
            savedState = get()
        )
    }

    viewModel {
        PasswordRestoreViewModel(
            authApi = get(),
            navigator = get(),
            savedState = get()
        )
    }
}