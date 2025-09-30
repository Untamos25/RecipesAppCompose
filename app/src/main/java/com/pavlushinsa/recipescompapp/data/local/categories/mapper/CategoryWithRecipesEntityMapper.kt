package com.pavlushinsa.recipescompapp.data.local.categories.mapper

import com.pavlushinsa.recipescompapp.data.local.categories.CategoryWithRecipesEntity
import com.pavlushinsa.recipescompapp.data.local.recipes.mapper.toDomain
import com.pavlushinsa.recipescompapp.domain.categories.model.CategoryWithRecipes

fun CategoryWithRecipesEntity.toDomain() = CategoryWithRecipes(
    category = categoryEntity.toDomain(),
    recipes = recipeEntities.map { it.toDomain() }
)
