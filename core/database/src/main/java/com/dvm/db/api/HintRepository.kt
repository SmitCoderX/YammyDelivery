package com.dvm.db.api

import com.dvm.db.api.models.Hint
import kotlinx.coroutines.flow.Flow

interface HintRepository {
    fun hints(): Flow<List<String>>
    suspend fun hintCount(): Int
    suspend fun insert(hint: Hint)
    suspend fun delete(hint: String)
    suspend fun deleteOldest()
}