package com.pavlushinsa.recipescompapp.data.local.categories.mapper

import com.pavlushinsa.recipescompapp.data.local.categories.entity.CategoryEntity
import com.pavlushinsa.recipescompapp.domain.categories.model.Category

fun CategoryEntity.toDomain() = Category(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    lastSyncTime = lastSyncTime
)
