package com.example.composeapp.presentation.categories.mapper

import com.example.composeapp.domain.categories.model.Category
import com.example.composeapp.presentation.categories.model.CategoryUiModel
import com.example.composeapp.presentation.common.constants.UIConstants.ASSETS_URI_PREFIX

fun Category.toUiModel() = CategoryUiModel(
    id = id,
    title = title,
    description = description,
    imageUrl = ASSETS_URI_PREFIX + imageUrl
)
