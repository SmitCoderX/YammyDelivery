package com.dvm.order.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvm.database.api.CartRepository
import com.dvm.database.api.OrderRepository
import com.dvm.database.api.mappers.toDbEntity
import com.dvm.database.api.models.CartItem
import com.dvm.navigation.api.Navigator
import com.dvm.navigation.api.model.Destination
import com.dvm.network.api.OrderApi
import com.dvm.order.order.model.OrderEvent
import com.dvm.order.order.model.OrderState
import com.dvm.preferences.api.DatastoreRepository
import com.dvm.utils.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.dvm.ui.R as CoreR

@HiltViewModel
internal class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val orderApi: OrderApi,
    private val datastore: DatastoreRepository,
    private val navigator: Navigator,
    savedState: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(OrderState())
        private set

    private val orderId = savedState.getLiveData<String>(Destination.Order.ORDER_ID)

    init {
        val id = requireNotNull(orderId.value)

        datastore
            .authorized()
            .filter { !it }
            .onEach {
                navigator.goTo(Destination.Login(Destination.Order(id)))
            }
            .launchIn(viewModelScope)

        orderRepository
            .order(id)
            .distinctUntilChanged()
            .onEach { state = state.copy(order = it) }
            .launchIn(viewModelScope)
    }

    fun dispatch(event: OrderEvent) {
        when (event) {
            OrderEvent.CancelOrder -> {
                cancelOrder()
            }
            OrderEvent.TryOrderAgain -> {
                checkCart()
            }
            OrderEvent.OrderAgain -> {
                viewModelScope.launch {
                    cartRepository.clearCart()
                    orderAgain()
                }
            }
            OrderEvent.DismissAlert -> {
                state = state.copy(
                    alert = null,
                    orderAgainMessage = null
                )
            }
            OrderEvent.CancelOrdering -> {
                navigator.back()
            }
            OrderEvent.Back -> {
                navigator.back()
            }
        }
    }

    private fun cancelOrder() {
        state = state.copy(progress = true)
        viewModelScope.launch {
            try {
                val order = orderApi.cancelOrder(
                    token = requireNotNull(datastore.getAccessToken()),
                    orderId = requireNotNull(state.order?.id)
                )
                orderRepository.insertOrders(listOf(order.toDbEntity()))

                state = state.copy(
                    progress = false,
                    cancelMessage = CoreR.string.order_message_order_canceled
                )
            } catch (exception: CancellationException) {
                throw CancellationException()
            } catch (exception: Exception) {
                state = state.copy(
                    progress = false,
                    alert = exception.getErrorMessage()
                )
            }
        }
    }

    private fun checkCart() {
        viewModelScope.launch {
            val cartCount = cartRepository.getDishCount()
            if (cartCount == 0) {
                orderAgain()
                return@launch
            }
            state = state.copy(orderAgainMessage = OrderState.Message(cartCount))
        }
    }

    private fun orderAgain() {
        viewModelScope.launch {
            val orderItems = state.order?.items.orEmpty()
            val cartItems =
                orderItems
                    .map {
                        CartItem(
                            dishId = it.dishId,
                            quantity = it.amount
                        )
                    }
            cartRepository.addToCart(cartItems)
            navigator.goTo(Destination.Cart)
        }
    }
}

