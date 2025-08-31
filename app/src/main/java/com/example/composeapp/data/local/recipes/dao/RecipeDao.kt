package com.example.composeapp.data.local.recipes.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.composeapp.data.local.recipes.entity.IngredientEntity
import com.example.composeapp.data.local.recipes.entity.RecipeEntity
import com.example.composeapp.data.local.recipes.RecipeWithIngredientsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipeWithIngredients(recipeId: Int): Flow<RecipeWithIngredientsEntity?>

    @Query("SELECT * FROM recipes WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteRecipes(): Flow<List<RecipeEntity>>

    @Query("UPDATE recipes SET isFavorite = :isFavorite WHERE id = :recipeId")
    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecipes(recipes: List<RecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertIngredients(ingredients: List<IngredientEntity>)

    @Query("DELETE FROM ingredients WHERE recipeId = :recipeId")
    suspend fun clearIngredientsForRecipe(recipeId: Int)

    @Query("DELETE FROM recipes WHERE id IN (:recipeIds)")
    suspend fun deleteRecipesByIds(recipeIds: List<Int>)

    @Transaction
    suspend fun replaceIngredientsForRecipe(recipeId: Int, ingredients: List<IngredientEntity>) {
        clearIngredientsForRecipe(recipeId)
        upsertIngredients(ingredients)
    }

    @Transaction
    suspend fun replaceRecipesAndIngredients(
        recipes: List<RecipeEntity>,
        ingredients: List<IngredientEntity>
    ) {
        upsertRecipes(recipes)

        val recipeIds = recipes.map { it.id }
        clearIngredientsForRecipes(recipeIds)
        if (ingredients.isNotEmpty()) upsertIngredients(ingredients)
    }

    @Query("DELETE FROM ingredients WHERE recipeId IN (:recipeIds)")
    suspend fun clearIngredientsForRecipes(recipeIds: List<Int>)
}
