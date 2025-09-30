package com.pavlushinsa.recipescompapp.data.local.categories.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pavlushinsa.recipescompapp.data.local.categories.CategoryWithRecipesEntity
import com.pavlushinsa.recipescompapp.data.local.categories.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY title ASC")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Transaction
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    fun getCategoryWithRecipes(categoryId: Int): Flow<CategoryWithRecipesEntity?>

    @Query("DELETE FROM categories WHERE id IN (:categoryIds)")
    suspend fun deleteCategoriesByIds(categoryIds: List<Int>)

    @Update
    suspend fun updateCategories(categories: List<CategoryEntity>)

    @Query("UPDATE categories SET lastSyncTime = :syncTime WHERE id = :categoryId")
    suspend fun updateCategorySyncTime(categoryId: Int, syncTime: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<CategoryEntity>)
}
