package com.dvm.database.impl.repositories

import com.dvm.database.api.CartRepository
import com.dvm.database.api.models.CartItem
import com.dvm.database.api.models.CartItemDetails
import com.dvm.database.impl.dao.CartDao
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class DefaultCartRepository @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override fun cartItems(): Flow<List<CartItemDetails>> = cartDao.cartItems()

    override fun totalQuantity(): Flow<Int?> = cartDao.totalQuantity()

    override suspend fun getDishCount(): Int =
        withContext(Dispatchers.IO) {
            cartDao.getCartCount()
        }

    override suspend fun addToCart(cartItem: CartItem) =
        withContext(Dispatchers.IO) {
            cartDao.insertCartItem(cartItem)
        }

    override suspend fun addToCart(cartItems: List<CartItem>) =
        withContext(Dispatchers.IO) {
            cartDao.insertCartItems(cartItems)
        }

    override suspend fun clearCart() =
        withContext(Dispatchers.IO) {
            cartDao.clearCart()
        }

    override suspend fun addPiece(dishId: String) =
        withContext(Dispatchers.IO) {
            cartDao.addPiece(dishId)
        }

    override suspend fun removePiece(dishId: String) =
        withContext(Dispatchers.IO) {
            cartDao.removePiece(dishId)
        }
}