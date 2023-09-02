package com.example.expensetracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity

import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "budget_table")
data class BudgetModel(
    @PrimaryKey(autoGenerate = true) val budgetId: Long = 0,
    @ColumnInfo(name = "budget_amount") var amount: Long,
    @ColumnInfo(name = "budget_name") var name: String,
    @ColumnInfo(name = "user_id") val userId: Long
): Serializable