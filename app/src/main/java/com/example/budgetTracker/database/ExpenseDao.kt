package com.example.budgetTracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.budgetTracker.Expense
import java.util.*

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses")
    fun getExpenses(): LiveData<List<Expense>>
    @Query("SELECT * FROM expenses WHERE id=(:id)")
    fun getExpense(id: UUID): LiveData<Expense?>
    @Update
    fun updateExpense(expense: Expense)
    @Insert
    fun addExpense(expense: Expense)

}