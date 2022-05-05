package com.example.budgetTracker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.budgetTracker.database.ExpenseDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "expense-database"

class ExpenseRepository private constructor(context: Context) {

    private val database : ExpenseDatabase = Room.databaseBuilder(
        context.applicationContext,
        ExpenseDatabase::class.java,
        DATABASE_NAME
    ).build()
    private val expenseDao = database.expenseDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getExpenses(): LiveData<List<Expense>> = expenseDao.getExpenses()
    fun getExpense(id: UUID): LiveData<Expense?> = expenseDao.getExpense(id)

    fun updateExpense(expense: Expense) {
        executor.execute {
            expenseDao.updateExpense(expense)
        }
    }
    fun addExpense(expense: Expense) {
        executor.execute {
            expenseDao.addExpense(expense)
        }
    }

    companion object {
        private var INSTANCE: ExpenseRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ExpenseRepository(context)
            }
        }
        fun get(): ExpenseRepository {
            return INSTANCE ?:
            throw IllegalStateException("ExpenseRepository must be initialized")
        }
    }
}