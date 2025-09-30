package com.pavlushinsa.recipescompapp.data.local.categories

import androidx.room.Embedded
import androidx.room.Relation
import com.pavlushinsa.recipescompapp.data.local.categories.entity.CategoryEntity
import com.pavlushinsa.recipescompapp.data.local.recipes.entity.RecipeEntity

data class CategoryWithRecipesEntity(
    @Embedded val categoryEntity: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val recipeEntities: List<RecipeEntity>
)
