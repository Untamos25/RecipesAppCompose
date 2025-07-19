package com.example.composeapp.presentation.categories.mapper

import com.example.composeapp.domain.categories.model.Category
import com.example.composeapp.presentation.categories.model.CategoryUiModel

fun Category.toUiModel() = CategoryUiModel(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl
)
