package com.example.composeapp.data.local.categories.mapper

import com.example.composeapp.data.local.categories.CategoryWithRecipesEntity
import com.example.composeapp.data.local.recipes.mapper.toDomain
import com.example.composeapp.domain.categories.model.CategoryWithRecipes

fun CategoryWithRecipesEntity.toDomain() = CategoryWithRecipes(
    category = categoryEntity.toDomain(),
    recipes = recipeEntities.map { it.toDomain() }
)
