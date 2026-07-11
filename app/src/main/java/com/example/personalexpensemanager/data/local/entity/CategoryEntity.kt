package com.example.personalexpensemanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.personalexpensemanager.domain.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val iconName: String,
    val name: String
)

fun CategoryEntity.toDomain(): Category = Category(
    id = id,
    iconName = iconName,
    name = name
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    iconName = iconName,
    name = name
)