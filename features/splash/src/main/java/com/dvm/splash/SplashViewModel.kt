package com.dvm.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvm.navigation.Destination
import com.dvm.navigation.Navigator
import com.dvm.preferences.datastore_api.data.DatastoreRepository
import com.dvm.updateservice.UpdateService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val updateService: UpdateService,
    private val datastore: DatastoreRepository,
    private val navigator: Navigator,
): ViewModel(){
    init {
        viewModelScope.launch {
            try {
                val updateDuration = measureTimeMillis {
                    withTimeout(TIMEOUT){
                        updateService.update()
                    }
                    datastore.saveUpdateError(false)
                }
                if (updateDuration < MIN_SPLASH_DURATION){
                    delay(MIN_SPLASH_DURATION - updateDuration)
                }
            } catch (exception: Exception) {
                datastore.saveUpdateError(true)
            } finally {
                navigator.goTo(Destination.Main)
            }
        }
    }
    companion object{
        private const val TIMEOUT = 10000L
        private const val MIN_SPLASH_DURATION = 3000L
    }
}