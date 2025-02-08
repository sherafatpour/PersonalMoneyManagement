package com.example.personalmoneymanagement


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personalmoneymanagement.ui.theme.CustomTypography
import com.example.personalmoneymanagement.ui.theme.kalamehFont
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinancialApp()
        }
    }
}


@Composable
fun FinancialApp(viewModel: FinancialViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    // Set RTL Layout Direction
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Income and Expense Calculation", style = CustomTypography.displayMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Input fields for monthly income, rent, medical expenses, and current expenses
            InputField(value = state.income, onValueChange = { viewModel.onIncomeChange(it) }, label = "Monthly Income")
            InputField(value = state.rent, onValueChange = { viewModel.onRentChange(it) }, label = "Rent")
            InputField(value = state.medicalExpenses, onValueChange = { viewModel.onMedicalExpensesChange(it) }, label = "Medical Expenses")
            InputField(value = state.currentExpenses, onValueChange = { viewModel.onCurrentExpensesChange(it) }, label = "Current Expenses")

            Spacer(modifier = Modifier.height(16.dp))

            // Button to trigger calculation
            Button(onClick = { viewModel.calculateResults() }) {
                Text("Calculate", style = CustomTypography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display the results if available
            if (state.result.isNotEmpty()) {
                Text(text = state.result, style = CustomTypography.bodyMedium)
            }
        }
    }




@Composable
fun InputField(value: String, onValueChange: (String) -> Unit, label: String) {
    // DecimalFormat instance for formatting the number
    val decimalFormat = remember { DecimalFormat("#,###") }

    // Format the current value as the user types
    val formattedValue = remember(value) {
        try {
            // Remove any non-digit characters for formatting (e.g., commas)
            val cleanedValue = value.replace("[^0-9]".toRegex(), "")
            val number = cleanedValue.toLongOrNull()
            number?.let { decimalFormat.format(it) } ?: value // format if valid
        } catch (e: Exception) {
            value // fallback to raw value in case of error
        }
    }

    OutlinedTextField(
        value =  formattedValue,
        onValueChange = {
            // Remove commas and store only digits
            val cleanedValue = it.replace("[^0-9]".toRegex(), "")
            onValueChange(cleanedValue) // pass the cleaned value to the parent
        },
        label = {
            Text(label, style = CustomTypography.bodyMedium)
        },
        textStyle = TextStyle(
            fontFamily = kalamehFont, // You can replace with your desired font family
            fontWeight = FontWeight.Normal,  // Change font weight

        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FinancialApp()
}