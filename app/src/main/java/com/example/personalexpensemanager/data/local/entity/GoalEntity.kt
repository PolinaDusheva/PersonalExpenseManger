package com.example.personalexpensemanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.personalexpensemanager.domain.Goal
import java.math.BigDecimal
import java.time.LocalDate

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey val id: String,
    val title: String,
    val targetAmount: BigDecimal,
    val deadline: LocalDate?
)

fun GoalEntity.toDomain(): Goal = Goal(
    id = id,
    title = title,
    targetAmount = targetAmount,
    deadline = deadline
)

fun Goal.toEntity(): GoalEntity = GoalEntity(
    id = id,
    title = title,
    targetAmount = targetAmount,
    deadline = deadline
)