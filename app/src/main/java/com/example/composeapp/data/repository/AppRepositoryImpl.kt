package com.example.composeapp.data.repository

import com.example.composeapp.data.categories.mapper.toDomainModel
import com.example.composeapp.data.repository.source.AppRepositoryStub
import com.example.composeapp.data.recipes.mapper.toDomainModel
import com.example.composeapp.domain.categories.model.Category
import com.example.composeapp.domain.repository.AppRepository
import com.example.composeapp.domain.recipes.model.Recipe

class AppRepositoryImpl(
    private val dataSource: AppRepositoryStub
) : AppRepository {

    override suspend fun getCategories(): List<Category> {
        val categoriesDto = dataSource.getCategories()
        return categoriesDto.map { it.toDomainModel() }
    }

    override suspend fun getCategoryById(categoryId: Int): Category? {
        val categoriesDto = dataSource.getCategoryById(categoryId)
        return categoriesDto?.toDomainModel()
    }

    override suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe> {
        val recipesDto = dataSource.getRecipesByCategoryId(categoryId)
        return recipesDto.map { it.toDomainModel() }
    }

    override suspend fun getRecipeById(recipeId: Int): Recipe? {
        val recipeDto = dataSource.getRecipeById(recipeId)
        return recipeDto?.toDomainModel()
    }
}
