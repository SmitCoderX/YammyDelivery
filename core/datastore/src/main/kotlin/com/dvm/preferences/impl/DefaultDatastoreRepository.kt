package com.dvm.preferences.impl

import com.dvm.preferences.api.DatastoreRepository
import com.dvm.utils.createFullToken
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
internal class DefaultDatastoreRepository @Inject constructor(
    private val dataStore: DataStore
) : DatastoreRepository {

    override fun authorized(): Flow<Boolean> = dataStore.authorized()

    override suspend fun isAuthorized(): Boolean = dataStore.isAuthorized()

    override suspend fun getAccessToken(): String? =
        dataStore.getAccessToken()
            ?.takeIf { it.isNotEmpty() }
            ?.let { createFullToken(it) }

    override suspend fun saveAccessToken(token: String) {
        dataStore.saveAccessToken(token)
    }

    override suspend fun getRefreshToken(): String? = dataStore.getRefreshToken()

    override suspend fun saveRefreshToken(token: String) {
        dataStore.saveRefreshToken(token)
    }

    override suspend fun deleteAccessToken() {
        dataStore.deleteAccessToken()
    }

    override suspend fun setUpdateError(error: Boolean) {
        dataStore.setUpdateError(error)
    }

    override suspend fun isUpdateError(): Boolean = dataStore.isUpdateError()

    override suspend fun setLastUpdateTime(time: Long) {
        dataStore.setLastUpdateTime(time)
    }

    override suspend fun getLastUpdateTime(): Long = dataStore.getLastUpdateTime()
}