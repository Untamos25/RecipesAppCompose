package com.pavlushinsa.recipescompapp.data.local.recipes

import androidx.room.Embedded
import androidx.room.Relation
import com.pavlushinsa.recipescompapp.data.local.recipes.entity.IngredientEntity
import com.pavlushinsa.recipescompapp.data.local.recipes.entity.RecipeEntity

data class RecipeWithIngredientsEntity(
    @Embedded val recipeEntity: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredientEntities: List<IngredientEntity>
)
