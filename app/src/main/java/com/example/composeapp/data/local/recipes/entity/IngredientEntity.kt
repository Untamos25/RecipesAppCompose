package com.example.composeapp.data.local.recipes.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "ingredients",
    primaryKeys = ["recipeId", "ingredientIndex"],
    foreignKeys = [ForeignKey(
        entity = RecipeEntity::class,
        parentColumns = ["id"],
        childColumns = ["recipeId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class IngredientEntity(
    val recipeId: Int,
    val ingredientIndex: Int,
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
)
