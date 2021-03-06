package com.dvm.database.api

import com.dvm.database.api.models.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun profile(): Flow<Profile?>
    suspend fun updateProfile(profile: Profile)
    suspend fun deleteProfile()
}