package com.example.composeapp.data.local.categories.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String
)
