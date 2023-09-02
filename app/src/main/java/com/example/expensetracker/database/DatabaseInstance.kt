package com.example.expensetracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.expensetracker.model.BudgetModel
import com.example.expensetracker.model.ExpenseModel
import com.example.expensetracker.model.UserModel

@Database(entities = [UserModel::class, BudgetModel::class, ExpenseModel::class], version = 1)
abstract class DatabaseInstance : RoomDatabase(){

    abstract fun databaseDao(): DatabaseDao
    companion object {
        @Volatile
        private var INSTANCE: DatabaseInstance? = null

        fun getDatabase(context: Context): DatabaseInstance {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseInstance::class.java,
                    "expense_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}