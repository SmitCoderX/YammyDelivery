package com.dvm.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.dvm.auth.login.model.LoginEvent
import com.dvm.auth.login.model.LoginState
import com.dvm.database.api.ProfileRepository
import com.dvm.database.api.models.Profile
import com.dvm.navigation.api.Navigator
import com.dvm.navigation.api.model.Destination
import com.dvm.network.api.AuthApi
import com.dvm.preferences.api.DatastoreRepository
import com.dvm.updateservice.api.UpdateService
import com.dvm.utils.AppException
import com.dvm.utils.extensions.getEmailErrorOrNull
import com.dvm.utils.extensions.getPasswordErrorOrNull
import com.dvm.utils.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import com.dvm.ui.R as CoreR

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val authApi: AuthApi,
    private val profileRepository: ProfileRepository,
    private val datastore: DatastoreRepository,
    private val updateService: UpdateService,
    private val navigator: Navigator,
    savedState: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val emailError = savedState.getLiveData<Int>("login_email_error")
    private val passwordError = savedState.getLiveData<Int>("login_password_error")

    init {
        combine(
            emailError.asFlow()
                .distinctUntilChanged(),
            passwordError.asFlow()
                .distinctUntilChanged()
        ) { emailError, passwordError ->
            state = state.copy(
                emailError = emailError,
                passwordError = passwordError
            )
        }
            .launchIn(viewModelScope)
    }

    fun dispatch(event: LoginEvent) {
        when (event) {
            is LoginEvent.ChangeLogin -> {
                emailError.value = null
            }
            is LoginEvent.ChangePassword -> {
                passwordError.value = null
            }
            LoginEvent.RestorePassword -> {
                navigator.goTo(Destination.PasswordRestoration)
            }
            LoginEvent.Register -> {
                navigator.goTo(Destination.Registration)
            }
            LoginEvent.DismissAlert -> {
                state = state.copy(alert = null)
            }
            LoginEvent.Back -> {
                navigator.back()
            }
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {

        val emailError = email.getEmailErrorOrNull()
        val passwordError = password.getPasswordErrorOrNull()

        if (emailError != null || passwordError != null) {
            this.emailError.value = emailError
            this.passwordError.value = passwordError
            return
        }

        state = state.copy(progress = true)

        viewModelScope.launch {
            try {
                val loginData = authApi.login(
                    login = email,
                    password = password
                )

                datastore.saveAccessToken(loginData.accessToken)
                datastore.saveRefreshToken(loginData.refreshToken)
                profileRepository.updateProfile(
                    Profile(
                        firstName = loginData.firstName,
                        lastName = loginData.lastName,
                        email = loginData.email
                    )
                )
                onSuccess()
                updateService.syncFavorites()
                navigator.goTo(Destination.LoginTarget)
            } catch (exception: CancellationException) {
                throw CancellationException()
            } catch (exception: Exception) {
                val message =
                    if (exception is AppException.IncorrectData) {
                        CoreR.string.auth_error_incorrect_data
                    } else {
                        exception.getErrorMessage()
                    }
                state = state.copy(
                    alert = message,
                    progress = false
                )
            }
        }
    }
}