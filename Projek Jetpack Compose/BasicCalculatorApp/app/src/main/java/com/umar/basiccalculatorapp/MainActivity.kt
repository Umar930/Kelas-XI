package com.umar.basiccalculatorapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umar.basiccalculatorapp.ui.theme.BasicCalculatorAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasicCalculatorAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorApp(modifier: Modifier = Modifier) {
    // Context untuk menampilkan Toast
    val context = LocalContext.current
    
    // State untuk menyimpan nilai input angka
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    
    // State untuk menyimpan hasil perhitungan
    var result by remember { mutableStateOf("Hasil: ") }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Kalkulator Dasar",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Input angka pertama
        OutlinedTextField(
            value = num1,
            onValueChange = { num1 = it },
            label = { Text("Angka Pertama") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Input angka kedua
        OutlinedTextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Angka Kedua") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Tombol operasi matematika
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Tombol Tambah
            Button(
                onClick = {
                    try {
                        val n1 = num1.toInt()
                        val n2 = num2.toInt()
                        val sum = n1 + n2
                        result = "Hasil: $sum"
                        Toast.makeText(context, "Hasil: $sum", Toast.LENGTH_SHORT).show()
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "Masukkan angka yang valid", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Add")
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Tombol Kurang
            Button(
                onClick = {
                    try {
                        val n1 = num1.toInt()
                        val n2 = num2.toInt()
                        val diff = n1 - n2
                        result = "Hasil: $diff"
                        Toast.makeText(context, "Hasil: $diff", Toast.LENGTH_SHORT).show()
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "Masukkan angka yang valid", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Sub")
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Tombol Kali
            Button(
                onClick = {
                    try {
                        val n1 = num1.toInt()
                        val n2 = num2.toInt()
                        val product = n1 * n2
                        result = "Hasil: $product"
                        Toast.makeText(context, "Hasil: $product", Toast.LENGTH_SHORT).show()
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "Masukkan angka yang valid", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Mul")
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Tombol Bagi
            Button(
                onClick = {
                    try {
                        val n1 = num1.toInt()
                        val n2 = num2.toInt()
                        if (n2 == 0) {
                            Toast.makeText(context, "Tidak dapat membagi dengan nol", Toast.LENGTH_SHORT).show()
                        } else {
                            val quotient = n1.toFloat() / n2.toFloat()
                            result = "Hasil: $quotient"
                            Toast.makeText(context, "Hasil: $quotient", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "Masukkan angka yang valid", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Div")
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Menampilkan hasil
        Text(
            text = result,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorAppPreview() {
    BasicCalculatorAppTheme {
        CalculatorApp()
    }
}