package com.dvm.network.api.response

data class OrderResponse(
    val id: String,
    val total: Int,
    val address: String,
    val statusId: String,
    val active: Boolean,
    val completed: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val items: List<OrderItemResponse>
)