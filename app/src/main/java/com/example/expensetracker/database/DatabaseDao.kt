package com.example.expensetracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expensetracker.model.BudgetModel
import com.example.expensetracker.model.ExpenseModel
import com.example.expensetracker.model.UserModel

@Dao
interface DatabaseDao {

    @Insert
    fun registerUser(user: UserModel)

    @Query("SELECT * FROM users")
    fun getUsersFromDB(): List<UserModel>

    @Insert
    fun addBudget(budget: BudgetModel): Long

    @Query("SELECT * FROM budget_table WHERE user_id = :userId")
    fun getAllBudgets(userId: Long): List<BudgetModel>

    @Insert
    fun addExpense(expense: ExpenseModel): Long

    @Query("SELECT * FROM expenses_table WHERE user_id = :userId")
    fun getAllExpenses(userId: Long): List<ExpenseModel>

    @Query("SELECT SUM(budget_amount) FROM budget_table WHERE user_id = :userId")
    fun getTotalBudget(userId: Long): Double

    @Query("SELECT SUM(expense_amount) FROM expenses_table WHERE user_id = :userId")
    fun getTotalExpenses(userId: Long): Double

    @Delete
    fun deleteExpense(expense: ExpenseModel)

    @Update
    fun updateExpense(expense: ExpenseModel)

    @Delete
    fun deleteBudget(budget: BudgetModel)

    @Update
    fun updateBudget(budget: BudgetModel)

}