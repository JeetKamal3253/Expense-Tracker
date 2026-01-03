
package com.example.expensetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun all(): Flow<List<Expense>>

    @Query("SELECT SUM(amountInInr) FROM expenses WHERE date(timestamp/1000,'unixepoch') = date(:day/1000,'unixepoch')")
    fun totalForDay(day: Long): Flow<Double?>

    @Query("SELECT SUM(amountInInr) FROM expenses WHERE strftime('%m', timestamp/1000,'unixepoch') = :month AND strftime('%Y', timestamp/1000,'unixepoch') = :year")
    fun totalForMonth(month: String, year: String): Flow<Double?>

    @Query("SELECT SUM(amountInInr) FROM expenses WHERE strftime('%Y', timestamp/1000,'unixepoch') = :year")
    fun totalForYear(year: String): Flow<Double?>

    @Query("SELECT * FROM expenses WHERE (:category IS NULL OR category = :category) AND timestamp BETWEEN :from AND :to ORDER BY timestamp DESC")
    fun filtered(category: String?, from: Long, to: Long): Flow<List<Expense>>
}
