package com.pavlushinsa.recipescompapp.data.local.recipes.mapper

import com.pavlushinsa.recipescompapp.data.local.recipes.entity.RecipeEntity
import com.pavlushinsa.recipescompapp.domain.recipes.model.Recipe

fun RecipeEntity.toDomain() = Recipe(
    id = id,
    categoryId = categoryId,
    title = title,
    method = method,
    imageUrl = imageUrl,
    isFavorite = isFavorite,
    lastSyncTime = lastSyncTime
)
