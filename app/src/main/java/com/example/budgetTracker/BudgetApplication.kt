package com.example.budgetTracker

import android.app.Application

class BudgetApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ExpenseRepository.initialize(this)
    }
}