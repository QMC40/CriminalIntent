package com.example.budgetTracker

import androidx.lifecycle.ViewModel

class ExpenseListViewModel : ViewModel() {

    private val expenseRepository = ExpenseRepository.get()
    val expenseListLiveData = expenseRepository.getExpenses()

    fun addExpense(expense: Expense) {
        expenseRepository.addExpense(expense)
    }
}