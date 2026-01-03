
package com.example.expensetracker.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.viewmodel.ExpenseViewModel
import java.util.*

@Composable
fun AddExpenseScreen(onBack: () -> Unit, vm: ExpenseViewModel = viewModel()) {
    val ctx = LocalContext.current
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("General") }
    var amount by remember { mutableStateOf("") }
    var selectedMillis by remember { mutableStateOf(System.currentTimeMillis()) }

    fun pickDateTime() {
        val cal = Calendar.getInstance().apply { timeInMillis = selectedMillis }
        DatePickerDialog(ctx, { _, y, m, d ->
            cal.set(Calendar.YEAR, y)
            cal.set(Calendar.MONTH, m)
            cal.set(Calendar.DAY_OF_MONTH, d)
            TimePickerDialog(ctx, { _, h, min ->
                cal.set(Calendar.HOUR_OF_DAY, h)
                cal.set(Calendar.MINUTE, min)
                selectedMillis = cal.timeInMillis
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Expense Title") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount (INR)") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { pickDateTime() }) { Text("Pick Date & Time") }
            Button(onClick = {
                val amt = amount.toDoubleOrNull()
                if (title.isNotBlank() && amt != null) {
                    vm.addExpense(title, category, amt, selectedMillis)
                    onBack()
                }
            }) { Text("Save") }
        }
        Spacer(Modifier.height(8.dp))
        Text("Selected: " + java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date(selectedMillis)))
    }
}
