package com.dvm.database.api

import com.dvm.database.api.models.CardDishDetails
import com.dvm.database.api.models.Dish
import com.dvm.database.api.models.DishDetails
import com.dvm.database.api.models.Recommended
import kotlinx.coroutines.flow.Flow

interface DishRepository {
    fun dish(dishId: String): Flow<DishDetails>
    fun search(query: String): Flow<List<CardDishDetails>>
    fun recommended(): Flow<List<CardDishDetails>>
    fun best(): Flow<List<CardDishDetails>>
    fun popular(): Flow<List<CardDishDetails>>
    fun favorite(): Flow<List<CardDishDetails>>
    suspend fun getDishes(category: String): Flow<List<CardDishDetails>>
    suspend fun hasSpecialOffers(): Boolean
    suspend fun getSpecialOffers(): List<CardDishDetails>
    suspend fun insertDishes(dishes: List<Dish>)
    suspend fun insertRecommended(dishIds: List<Recommended>)
}