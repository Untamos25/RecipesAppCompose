package com.example.composeapp.data.remote.categories.mapper

import com.example.composeapp.data.remote.categories.model.CategoryDto
import com.example.composeapp.data.local.categories.entity.CategoryEntity

fun CategoryDto.toEntity() = CategoryEntity(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl
)
