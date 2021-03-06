package com.dvm.database.impl.repositories

import com.dvm.database.api.FavoriteRepository
import com.dvm.database.api.models.Favorite
import com.dvm.database.impl.dao.FavoriteDao
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class DefaultFavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override suspend fun getFavorites(): List<String> =
        withContext(Dispatchers.IO) {
            favoriteDao.getFavorites()
        }

    override suspend fun isFavorite(dishId: String): Boolean =
        withContext(Dispatchers.IO) {
            favoriteDao.isFavorite(dishId)
        }

    override suspend fun addToFavorite(dishId: String) =
        withContext(Dispatchers.IO) {
            favoriteDao.insert(Favorite(dishId))
        }

    override suspend fun deleteFromFavorite(dishId: String) =
        withContext(Dispatchers.IO) {
            favoriteDao.delete(Favorite(dishId))
        }

    override suspend fun addListToFavorite(favorites: List<Favorite>) =
        withContext(Dispatchers.IO) {
            favoriteDao.insertList(favorites)
        }

    override suspend fun deleteFavorites() =
        withContext(Dispatchers.IO) {
            favoriteDao.deleteFavorites()
        }
}