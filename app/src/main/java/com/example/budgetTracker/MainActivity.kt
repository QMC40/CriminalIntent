package com.example.budgetTracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.criminalintent.R
import java.util.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), ExpenseListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragment = ExpenseListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
    override fun onExpenseSelected(expenseId: UUID) {

        val fragment = ExpenseFragment.newInstance(expenseId)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}