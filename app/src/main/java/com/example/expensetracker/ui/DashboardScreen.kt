
package com.example.expensetracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(onAddExpense: () -> Unit, vm: ExpenseViewModel = viewModel()) {
    val (dayTotal, monthTotal, yearTotal) = vm.totals.collectAsState().value
    val expenses = vm.allExpenses.collectAsState().value

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ElevatedCard(Modifier.weight(1f).padding(end = 8.dp)) {
                Column(Modifier.padding(12.dp)) {
                    Text("Today", style = MaterialTheme.typography.titleMedium)
                    Text("₹ %.2f".format(dayTotal))
                }
            }
            ElevatedCard(Modifier.weight(1f).padding(horizontal = 4.dp)) {
                Column(Modifier.padding(12.dp)) {
                    Text("This Month", style = MaterialTheme.typography.titleMedium)
                    Text("₹ %.2f".format(monthTotal))
                }
            }
            ElevatedCard(Modifier.weight(1f).padding(start = 8.dp)) {
                Column(Modifier.padding(12.dp)) {
                    Text("This Year", style = MaterialTheme.typography.titleMedium)
                    Text("₹ %.2f".format(yearTotal))
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = onAddExpense) { Text("Add Expense") }
        Spacer(Modifier.height(12.dp))
        Text("Recent Expenses", style = MaterialTheme.typography.titleMedium)
        LazyColumn(Modifier.fillMaxSize()) {
            items(expenses) { e ->
                ListItem(
                    headlineContent = { Text(e.title) },
                    supportingContent = {
                        val dt = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(e.timestamp))
                        Text("₹ %.2f • %s • %s".format(e.amountInInr, e.category, dt))
                    }
                )
                Divider()
            }
        }
    }
}
