package com.example.expensetracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "expenses_table")
data class ExpenseModel(
    @PrimaryKey(autoGenerate = true) val expenseId: Long = 0,
    @ColumnInfo(name = "expense_amount") var amount: Long,
    @ColumnInfo(name = "expense_name") var name: String,
    @ColumnInfo(name = "user_id") val userId: Long
): Serializable