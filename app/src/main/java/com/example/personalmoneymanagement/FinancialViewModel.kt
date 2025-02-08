package com.example.personalmoneymanagement

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.DecimalFormat

// ViewModel to manage the state and logic
class FinancialViewModel : ViewModel() {

    private val _state = MutableStateFlow(FinancialState())
    val state: StateFlow<FinancialState> = _state

    fun onIncomeChange(value: String) {
        _state.value = _state.value.copy(income = value)
    }

    fun onRentChange(value: String) {
        _state.value = _state.value.copy(rent = value)
    }

    fun onMedicalExpensesChange(value: String) {
        _state.value = _state.value.copy(medicalExpenses = value)
    }

    fun onCurrentExpensesChange(value: String) {
        _state.value = _state.value.copy(currentExpenses = value)
    }

    fun calculateResults() {
        // Convert inputs to long or default to 0 if invalid
        val income = _state.value.income.toLongOrNull() ?: 0L
        val rent = _state.value.rent.toLongOrNull() ?: 0L
        val medicalExpenses = _state.value.medicalExpenses.toLongOrNull() ?: 0L
        val currentExpenses = _state.value.currentExpenses.toLongOrNull() ?: 0L

        // Calculate total expenses (rent + medical expenses + current expenses)
        val totalExpenses = rent + medicalExpenses + currentExpenses

        // Calculate remaining income after expenses
        val remainingIncome = (income - totalExpenses).coerceAtLeast(0L)

        // Allocate remaining income between gold and dollars
        val goldInvestmentPercent = 0.70 // 70% allocated to gold investment
        val dollarInvestmentPercent = 1 - goldInvestmentPercent // 30% allocated to dollar investment

        val goldInvestment = remainingIncome * goldInvestmentPercent
        val dollarInvestment = remainingIncome * dollarInvestmentPercent

        // Format results for display
        val df = DecimalFormat("#,###")
        val result = """
            Monthly Income: ${df.format(income)} Rial
            Rent: ${df.format(rent)} Rial
            Medical Expenses: ${df.format(medicalExpenses)} Rial
            Current Expenses: ${df.format(currentExpenses)} Rial
            Gold Investment (70%): ${df.format(goldInvestment)} Rial
            Dollar Investment (30%): ${df.format(dollarInvestment)} Rial
        """.trimIndent()

        // Update state with the calculated result
        _state.value = _state.value.copy(result = result)
    }
}

data class FinancialState(
    val income: String = "",
    val rent: String = "",
    val medicalExpenses: String = "",
    val currentExpenses: String = "",
    val result: String = ""
)
