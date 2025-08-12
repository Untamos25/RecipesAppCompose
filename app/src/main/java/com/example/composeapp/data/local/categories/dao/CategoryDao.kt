package com.example.composeapp.data.local.categories.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.composeapp.data.local.categories.CategoryWithRecipesEntity
import com.example.composeapp.data.local.categories.entity.CategoryEntity
import com.example.composeapp.data.local.recipes.dao.RecipeDao
import com.example.composeapp.data.local.recipes.entity.IngredientEntity
import com.example.composeapp.data.local.recipes.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY title ASC")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Transaction
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    fun getCategoryWithRecipes(categoryId: Int): Flow<CategoryWithRecipesEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCategories(categories: List<CategoryEntity>)

    @Transaction
    suspend fun updateAllData(
        recipeDao: RecipeDao,
        categories: List<CategoryEntity>,
        recipes: List<RecipeEntity>,
        ingredients: List<IngredientEntity>
    ) {
        upsertCategories(categories)
        recipeDao.upsertRecipes(recipes)
        recipeDao.upsertIngredients(ingredients)
    }
}
