package com.umar.stateexample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Komponen UI untuk menampilkan teks.
 * 
 * @param text Teks yang akan ditampilkan
 */
@Composable
fun MyText(text: String) {
    Text(
        text = text,
        fontSize = 30.sp,
        modifier = Modifier.fillMaxWidth(1f)
    )
}

/**
 * Komponen UI untuk input teks.
 * 
 * @param value Nilai saat ini yang ditampilkan di TextField
 * @param onValueChange Callback yang dipanggil saat nilai berubah
 * @param label Label untuk TextField
 */
@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(0.8f)
    )
}

/**
 * Layar utama untuk mendemonstrasikan berbagai metode manajemen state.
 * 
 * Contoh menggunakan state lokal dengan remember:
 * ```
 * val name = remember { mutableStateOf("") }
 * ```
 * Remember mempertahankan state selama recomposition, namun state akan hilang
 * saat terjadi perubahan konfigurasi (seperti rotasi layar).
 * 
 * Contoh menggunakan state lokal dengan rememberSaveable:
 * ```
 * val name = rememberSaveable { mutableStateOf("") }
 * ```
 * RememberSaveable mempertahankan state selama recomposition dan perubahan konfigurasi
 * (seperti rotasi layar).
 * 
 * @param viewModel ViewModel yang akan digunakan untuk mengelola state
 */
@Composable
fun StateTestScreen(
    modifier: Modifier = Modifier,
    viewModel: StateTestViewModel = viewModel()
) {
    // Mengambil state dari ViewModel dan mengonversinya ke Compose State
    // menggunakan observeAsState
    val name: String by viewModel.name.observeAsState("")
    val surname: String by viewModel.surname.observeAsState("")
    
    // Perbandingan dengan metode state lokal (dikomentari)
    // Akan hilang saat terjadi recomposition
    // val name = remember { mutableStateOf("") }
    
    // Tahan terhadap perubahan konfigurasi, namun tetap di komponen ini
    // val surname = rememberSaveable { mutableStateOf("") }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Menampilkan nama lengkap
        MyText(text = "Hello $name $surname")
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Input untuk nama depan
        MyTextField(
            value = name,
            onValueChange = viewModel::onNameUpdate,
            label = "Enter Name"
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Input untuk nama belakang
        MyTextField(
            value = surname,
            onValueChange = viewModel::onSurnameUpdate,
            label = "Enter Surname"
        )
    }
}