package com.example.composeapp.data.local.categories.mapper

import com.example.composeapp.data.local.categories.entity.CategoryEntity
import com.example.composeapp.domain.categories.model.Category

fun CategoryEntity.toDomain() = Category(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    lastSyncTime = lastSyncTime
)
