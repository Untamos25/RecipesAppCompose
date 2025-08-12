package com.example.composeapp.data.local.recipes.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingredients",
    foreignKeys = [ForeignKey(
        entity = RecipeEntity::class,
        parentColumns = ["id"],
        childColumns = ["recipeId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true) val ingredientId: Int = 0,
    val recipeId: Int,
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
)
