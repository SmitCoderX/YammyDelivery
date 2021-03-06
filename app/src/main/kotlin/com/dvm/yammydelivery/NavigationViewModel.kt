package com.dvm.yammydelivery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.dvm.navigation.api.Navigator
import com.dvm.navigation.api.model.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
internal class NavigationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    navigator: Navigator
) : ViewModel() {

    var navController: NavHostController? = null

    private val currentDestination
        get() = navController?.currentBackStackEntry?.destination?.route

    private var targetDestination: Destination?
        get() = savedStateHandle.get(TARGET_DESTINATION)
        set(value) = savedStateHandle.set(TARGET_DESTINATION, value)

    init {
        navigator
            .destination
            .onEach { destination ->
                val navController = navController ?: return@onEach

                if (destination is Destination.Login && destination.targetDestination != null) {
                    targetDestination = destination.targetDestination
                    navigateTo(
                        navController = navController,
                        destination = destination,
                        builder = {
                            popUpTo(currentDestination!!) { inclusive = true }
                        }
                    )
                } else {
                    if (
                        destination !is Destination.Login &&
                        destination !is Destination.Registration &&
                        destination !is Destination.PasswordRestoration &&
                        destination !is Destination.LoginTarget
                    ) {
                        targetDestination = null
                    }
                    navigateTo(
                        navController = navController,
                        destination = destination
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun navigateToNotification() {
        navController?.let {
            navigateTo(it, Destination.Notification)
        }
    }

    private fun navigateTo(
        navController: NavController,
        destination: Destination,
        builder: NavOptionsBuilder.() -> Unit = { }
    ) {
        when (destination) {
            is Destination.Dish -> {
                navController.navigate(
                    route = destination.createRoute(destination.dishId),
                    builder = builder
                )
            }
            is Destination.Order -> {
                navController.navigate(
                    route = destination.createRoute(destination.orderId),
                    builder = {
                        if (currentDestination == Destination.Ordering.route) {
                            popUpTo(currentDestination!!) { inclusive = true }
                        }
                    }
                )
            }
            is Destination.Category -> {
                navController.navigate(
                    route = destination.createRoute(
                        categoryId = destination.categoryId,
                        subcategoryId = destination.subcategoryId
                    ),
                    builder = builder
                )
            }
            Destination.Main -> {
                navController.navigate(Destination.Main.route) {
                    if (currentDestination == Destination.Splash.route) {
                        navController.popBackStack()
                    }
                }
            }
            is Destination.BackToOrdering -> {
                navController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(Destination.Map.MAP_ADDRESS, destination.address)
                navController.popBackStack()
            }
            Destination.FinishRegister -> {
                navController.popBackStack(Destination.Login.ROUTE, true)
            }
            is Destination.Map -> {
                navController.navigate(Destination.Map.ROUTE)
            }
            is Destination.Login -> {
                targetDestination = destination.targetDestination
                navController.navigate(destination.route, builder)
            }
            is Destination.LoginTarget -> {
                val targetDestination = targetDestination
                if (targetDestination != null) {
                    navigateTo(
                        navController = navController,
                        destination = targetDestination,
                        builder = {
                            popUpTo(currentDestination!!) { inclusive = true }
                        }
                    )
                } else {
                    navController.navigateUp()
                }
            }
            Destination.Back -> {
                navController.navigateUp()
            }
            else -> {
                navController.navigate(destination.route, builder)
            }
        }
    }

    companion object {
        private const val TARGET_DESTINATION = "targetDestination"
    }
}
