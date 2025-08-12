package com.example.composeapp.data.local.recipes.mapper

import com.example.composeapp.data.local.recipes.entity.RecipeEntity
import com.example.composeapp.domain.recipes.model.Recipe

fun RecipeEntity.toDomain() = Recipe(
    id = id,
    categoryId = categoryId,
    title = title,
    method = method,
    imageUrl = imageUrl,
    isFavorite = isFavorite
)
