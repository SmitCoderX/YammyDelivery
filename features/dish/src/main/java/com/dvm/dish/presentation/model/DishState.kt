package com.dvm.dish.presentation.model

import androidx.compose.runtime.Immutable
import com.dvm.db.api.models.DishDetails
import com.dvm.db.api.models.Review

@Immutable
internal data class DishState(
    val dish: DishDetails,
    val quantity: Int = 1,
    val reviews: List<Review> = emptyList(),
    val alertMessage: String? = null
)