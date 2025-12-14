package com.umar.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.umar.calculator.ui.theme.CalculatorTheme
import kotlinx.coroutines.delay
import net.objecthunter.exp4j.ExpressionBuilder

// Definisi Rute Navigasi
object NavRoutes {
    const val SplashScreenRoute = "splash_screen"
    const val MainScreenRoute = "main_screen"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppScreen()
                }
            }
        }
    }
}

@Composable
fun AppScreen() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = NavRoutes.SplashScreenRoute
    ) {
        composable(NavRoutes.SplashScreenRoute) {
            SplashScreen(navController = navController)
        }
        composable(NavRoutes.MainScreenRoute) {
            CalculatorScreen()
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    // Memuat komposisi Lottie
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("elegant_calculator_splash.json"))
    
    // Box sebagai kontainer utama
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animasi Lottie
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(250.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "KALKULATOR",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = "By Umar",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        // Logika penundaan dan navigasi
        LaunchedEffect(Unit) {
            delay(3000L) // Penundaan 3 detik
            
            // Navigasi ke layar utama
            navController.navigate(NavRoutes.MainScreenRoute) {
                // Menghapus Splash Screen dari back stack
                popUpTo(NavRoutes.SplashScreenRoute) { inclusive = true }
            }
        }
    }
}

// State kalkulator untuk mengelola operasi dan tampilan
class CalculatorState {
    var number1 by mutableStateOf("")
    var operation by mutableStateOf<String?>(null)
    var number2 by mutableStateOf("")
    var displayValue by mutableStateOf("0")

    // Fungsi untuk menambahkan digit ke input saat ini
    fun addDigit(digit: String) {
        if (operation == null) {
            // Input untuk number1
            if (number1 == "0" && digit != ".") {
                number1 = digit
            } else {
                // Hindari duplikasi titik desimal
                if (digit == "." && number1.contains(".")) {
                    return
                }
                number1 += digit
            }
            displayValue = number1
        } else {
            // Input untuk number2
            if (number2 == "0" && digit != ".") {
                number2 = digit
            } else {
                // Hindari duplikasi titik desimal
                if (digit == "." && number2.contains(".")) {
                    return
                }
                number2 += digit
            }
            displayValue = number2
        }
    }

    // Fungsi untuk mengatur operasi
    fun applyOperation(op: String) {
        // Jika number1 kosong, gunakan 0 sebagai default
        if (number1.isEmpty()) {
            number1 = "0"
        }
        operation = op
    }
    
    // Fungsi untuk menghitung hasil
    fun calculate() {
        if (number1.isNotEmpty() && operation != null) {
            // Jika number2 belum diisi, gunakan number1 sebagai operand kedua
            val secondNumber = if (number2.isNotEmpty()) number2 else number1
            
            try {
                // Bangun ekspresi matematika
                val expressionString = "$number1${operation}$secondNumber"
                val expression = ExpressionBuilder(expressionString.replace("×", "*").replace("÷", "/")).build()
                val result = expression.evaluate()
                
                // Format hasil
                val formattedResult = if (result == result.toLong().toDouble()) {
                    result.toLong().toString()
                } else {
                    result.toString()
                }
                
                displayValue = formattedResult
                number1 = formattedResult
                operation = null
                number2 = ""
            } catch (e: Exception) {
                displayValue = "Error"
                number1 = ""
                operation = null
                number2 = ""
            }
        }
    }
    
    // Fungsi untuk menghapus semua input
    fun clear() {
        number1 = ""
        operation = null
        number2 = ""
        displayValue = "0"
    }
    
    // Fungsi untuk mengganti tanda positif/negatif
    fun toggleSign() {
        if (operation == null) {
            if (number1.isNotEmpty() && number1 != "0") {
                number1 = if (number1.startsWith("-")) {
                    number1.substring(1)
                } else {
                    "-$number1"
                }
                displayValue = number1
            }
        } else {
            if (number2.isNotEmpty() && number2 != "0") {
                number2 = if (number2.startsWith("-")) {
                    number2.substring(1)
                } else {
                    "-$number2"
                }
                displayValue = number2
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    // Ingat state kalkulator
    val calculatorState = remember { CalculatorState() }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Area tampilan hasil
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = calculatorState.displayValue,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )
        }
        
        // Area tombol kalkulator
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Baris 1: C, (), %, ÷
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalculatorButton(
                    symbol = "AC",
                    color = Color.Red,
                    textColor = Color.White,
                    onClick = { calculatorState.clear() }
                )
                CalculatorButton(
                    symbol = "( )",
                    color = Color.Gray,
                    onClick = { /* Fitur kurung belum diimplementasikan */ }
                )
                CalculatorButton(
                    symbol = "%",
                    color = Color.Gray,
                    onClick = { /* Fitur persentase belum diimplementasikan */ }
                )
                CalculatorButton(
                    symbol = "÷",
                    color = Color(0xFFF79931), // Warna oranye
                    onClick = { calculatorState.applyOperation("/") }
                )
            }
            
            // Baris 2: 7, 8, 9, ×
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalculatorButton(
                    symbol = "7",
                    color = Color(0xFF00BCD4), // Warna cyan
                    onClick = { calculatorState.addDigit("7") }
                )
                CalculatorButton(
                    symbol = "8",
                    color = Color(0xFF00BCD4),
                    onClick = { calculatorState.addDigit("8") }
                )
                CalculatorButton(
                    symbol = "9",
                    color = Color(0xFF00BCD4),
                    onClick = { calculatorState.addDigit("9") }
                )
                CalculatorButton(
                    symbol = "×",
                    color = Color(0xFFF79931),
                    onClick = { calculatorState.applyOperation("*") }
                )
            }
            
            // Baris 3: 4, 5, 6, -
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalculatorButton(
                    symbol = "4",
                    color = Color(0xFF00BCD4),
                    onClick = { calculatorState.addDigit("4") }
                )
                CalculatorButton(
                    symbol = "5",
                    color = Color(0xFF00BCD4),
                    onClick = { calculatorState.addDigit("5") }
                )
                CalculatorButton(
                    symbol = "6",
                    color = Color(0xFF00BCD4),
                    onClick = { calculatorState.addDigit("6") }
                )
                CalculatorButton(
                    symbol = "-",
                    color = Color(0xFFF79931),
                    onClick = { calculatorState.applyOperation("-") }
                )
            }
            
            // Baris 4: 1, 2, 3, +
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalculatorButton(
                    symbol = "1",
                    color = Color(0xFF00BCD4),
                    onClick = { calculatorState.addDigit("1") }
                )
                CalculatorButton(
                    symbol = "2",
                    color = Color(0xFF00BCD4),
                    onClick = { calculatorState.addDigit("2") }
                )
                CalculatorButton(
                    symbol = "3",
                    color = Color(0xFF00BCD4),
                    onClick = { calculatorState.addDigit("3") }
                )
                CalculatorButton(
                    symbol = "+",
                    color = Color(0xFFF79931),
                    onClick = { calculatorState.applyOperation("+") }
                )
            }
            
            // Baris 5: ±, 0, ., =
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalculatorButton(
                    symbol = "±",
                    color = Color.Red,
                    textColor = Color.White,
                    onClick = { calculatorState.toggleSign() }
                )
                CalculatorButton(
                    symbol = "0",
                    color = Color(0xFF00BCD4),
                    onClick = { calculatorState.addDigit("0") }
                )
                CalculatorButton(
                    symbol = ".",
                    color = Color(0xFF00BCD4),
                    onClick = { calculatorState.addDigit(".") }
                )
                CalculatorButton(
                    symbol = "=",
                    color = Color(0xFFF79931),
                    onClick = { calculatorState.calculate() }
                )
            }
        }
    }
}

@Composable
fun CalculatorButton(
    symbol: String,
    color: Color,
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(color)
            .clickable { onClick() }
    ) {
        Text(
            text = symbol,
            fontSize = 28.sp,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    CalculatorTheme {
        CalculatorScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    CalculatorTheme {
        SplashScreen(rememberNavController())
    }
}