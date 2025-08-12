package com.example.composeapp.data.local.categories

import androidx.room.Embedded
import androidx.room.Relation
import com.example.composeapp.data.local.categories.entity.CategoryEntity
import com.example.composeapp.data.local.recipes.entity.RecipeEntity

data class CategoryWithRecipesEntity(
    @Embedded val categoryEntity: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val recipeEntities: List<RecipeEntity>
)
