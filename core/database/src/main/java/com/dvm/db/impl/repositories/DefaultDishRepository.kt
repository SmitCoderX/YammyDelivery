package com.dvm.db.impl.repositories

import com.dvm.db.api.DishRepository
import com.dvm.db.api.models.CardDishDetails
import com.dvm.db.api.models.Dish
import com.dvm.db.api.models.DishDetails
import com.dvm.db.api.models.Recommended
import com.dvm.db.impl.dao.DishDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultDishRepository @Inject constructor(
    private val dishDao: DishDao
) : DishRepository {

    override fun dish(dishId: String): Flow<DishDetails> = dishDao.dish(dishId)

    override fun search(query: String): Flow<List<CardDishDetails>> = dishDao.search(query)

    override fun recommended(): Flow<List<CardDishDetails>> = dishDao.recommended()

    override fun best(): Flow<List<CardDishDetails>> = dishDao.best()

    override fun popular(): Flow<List<CardDishDetails>> = dishDao.popular()

    override fun favorite(): Flow<List<CardDishDetails>> = dishDao.favorite()

    override suspend fun getDishes(category: String): Flow<List<CardDishDetails>> =
        dishDao.getDishes(category)

    override suspend fun hasSpecialOffers(): Boolean =
        withContext(Dispatchers.IO) {
            dishDao.hasSpecialOffers()
        }

    override suspend fun getSpecialOffers(): List<CardDishDetails> =
        withContext(Dispatchers.IO) {
            dishDao.getSpecialOffers()
        }

    override suspend fun insertDishes(dishes: List<Dish>) =
        withContext(Dispatchers.IO) {
            dishDao.insertDishes(dishes)
        }

    override suspend fun insertRecommended(dishIds: List<Recommended>) =
        withContext(Dispatchers.IO) {
            dishDao.insertRecommended(dishIds)
        }
}