package com.dvm.db.db_impl.data

import com.dvm.db.db_api.data.OrderRepository
import com.dvm.db.db_api.data.models.*
import com.dvm.db.db_impl.data.dao.OrderDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultOrderRepository @Inject constructor(
    private val orderDao: OrderDao
): OrderRepository {

    override fun activeOrders(): Flow<List<OrderData>> = orderDao.activeOrders()

    override fun completedOrders(): Flow<List<OrderData>> = orderDao.completedOrders()

    override fun order(orderId: String): Flow<OrderWithItems> = orderDao.order(orderId)

    override suspend fun insertOrders(orders: List<Order>)  = withContext(Dispatchers.IO) {
        orderDao.insertOrders(orders)
    }

    override suspend fun insertOrderItems(orderItems: List<OrderItem>)  = withContext(Dispatchers.IO) {
        orderDao.insertOrderItems(orderItems)
    }

    override suspend fun insertOrderStatuses(orderStatuses: List<OrderStatus>) = withContext(Dispatchers.IO) {
        orderDao.insertOrderStatuses(orderStatuses)
    }
}