package com.pavlushinsa.recipescompapp.presentation.categories.mapper

import com.pavlushinsa.recipescompapp.domain.categories.model.Category
import com.pavlushinsa.recipescompapp.presentation.categories.model.CategoryUiModel

fun Category.toUiModel() = CategoryUiModel(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl
)
