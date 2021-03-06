package com.dvm.cart.model

import androidx.compose.runtime.Immutable
import com.dvm.database.api.models.CartItemDetails

@Immutable
internal data class CartState(
    val items: List<CartItemDetails> = emptyList(),
    val totalPrice: Int = 0,
    val promoCode: String = "",
    val promoCodeText: String = "",
    val appliedPromoCode: Boolean = false,
    val progress: Boolean = false,
    val alert: Int? = null
)