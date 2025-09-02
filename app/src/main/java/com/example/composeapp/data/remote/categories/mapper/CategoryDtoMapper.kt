package com.example.composeapp.data.remote.categories.mapper

import com.example.composeapp.data.local.categories.entity.CategoryEntity
import com.example.composeapp.data.remote.categories.model.CategoryDto

fun CategoryDto.toEntity() = CategoryEntity(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl
)
