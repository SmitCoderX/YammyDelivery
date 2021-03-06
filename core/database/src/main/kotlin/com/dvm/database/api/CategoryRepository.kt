package com.dvm.database.api

import com.dvm.database.api.models.Category
import com.dvm.database.api.models.ParentCategory
import com.dvm.database.api.models.Subcategory
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun searchParentCategory(query: String): Flow<List<ParentCategory>>
    fun searchSubcategory(query: String): Flow<List<Subcategory>>
    suspend fun getParentCategories(): List<ParentCategory>
    suspend fun getSubcategories(parentId: String): List<Subcategory>
    suspend fun getCategoryTitle(categoryId: String): String
    suspend fun insertCategories(categories: List<Category>)
}