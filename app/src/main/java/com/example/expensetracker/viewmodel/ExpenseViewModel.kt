
package com.example.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.AppDatabase
import com.example.expensetracker.data.Expense
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ExpenseViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).expenseDao()

    val allExpenses = dao.all().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun nowMillis() = System.currentTimeMillis()

    val totals: StateFlow<Triple<Double, Double, Double>> = combine(
        dao.totalForDay(nowMillis()),
        dao.totalForMonth(SimpleDateFormat("MM", Locale.getDefault()).format(Date()), SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())),
        dao.totalForYear(SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()))
    ) { day, month, year ->
        Triple(day ?: 0.0, month ?: 0.0, year ?: 0.0)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Triple(0.0,0.0,0.0))

    fun addExpense(title: String, category: String, amount: Double, dateTimeMillis: Long = nowMillis()) {
        viewModelScope.launch {
            dao.insert(Expense(title = title, category = category, amountInInr = amount, timestamp = dateTimeMillis))
        }
    }
}
