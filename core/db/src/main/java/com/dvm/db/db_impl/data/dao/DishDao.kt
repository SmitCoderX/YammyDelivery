package com.dvm.db.db_impl.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dvm.db.db_api.data.models.Dish
import com.dvm.db.db_api.data.models.DishDetails
import kotlinx.coroutines.flow.Flow

@Dao
internal interface DishDao {

    @Query(
        """
        SELECT
            *,
            EXISTS(
                SELECT 1
                FROM favorite
                WHERE favorite.dishId = :dishId
            ) as isFavorite,
            EXISTS(
                SELECT 1
                FROM dish
                WHERE dish.id = :dishId
                AND dish.oldPrice IS NOT NULL
                AND dish.oldPrice > dish.price
            ) as hasSpecialOffer
        FROM dish
        WHERE id = :dishId
        AND active = 1
    """
    )
    fun getDish(dishId: String): Flow<DishDetails>

    @Query(
        """
        SELECT *
        FROM dish
        WHERE category IS :category
        AND active = 1
    """
    )
    suspend fun getDishes(category: String): List<Dish>

    @Query(
        """
            SELECT EXISTS(
                SELECT 1
                FROM dish
                WHERE oldPrice > 0
            )

        """
    )
    suspend fun hasSpecialOffers(): Boolean

    @Query(
        """
        SELECT *
        FROM dish
        WHERE oldPrice > price
        AND active = 1
    """
    )
    suspend fun getSpecialOffers(): List<Dish>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDishes(dishes: List<Dish>)
}

