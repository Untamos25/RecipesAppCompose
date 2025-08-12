package com.example.composeapp.data.local.recipes.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.composeapp.data.local.categories.entity.CategoryEntity

@Entity(
    tableName = "recipes",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val categoryId: Int,
    val title: String,
    val method: List<String>,
    val imageUrl: String,
    val isFavorite: Boolean = false
)
