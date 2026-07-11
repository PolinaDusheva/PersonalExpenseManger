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
    val targetAmount: String,
    val deadline: Long?
)

fun GoalEntity.toDomain(): Goal = Goal(
    id = id,
    title = title,
    targetAmount = BigDecimal(targetAmount),
    deadline = deadline?.let { LocalDate.ofEpochDay(it) }
)

fun Goal.toEntity(): GoalEntity = GoalEntity(
    id = id,
    title = title,
    targetAmount = targetAmount.toPlainString(),
    deadline = deadline?.toEpochDay()
)