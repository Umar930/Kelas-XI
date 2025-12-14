package com.umar.ecommerceapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.umar.ecommerceapp.R
import com.umar.ecommerceapp.util.AppUtil
import com.umar.ecommerceapp.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(navController: NavHostController) {
    // Inisialisasi ViewModel
    val authViewModel: AuthViewModel = viewModel()
    
    // Dapatkan Context
    val context = LocalContext.current
    
    // Manajemen State
    val email = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Judul Utama
        Text(
            text = "Hello There",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        // Spacer
        Spacer(modifier = Modifier.height(10.dp))

        // Subjudul
        Text(
            text = "Create an account",
            modifier = Modifier.fillMaxWidth()
        )

        // Spacer
        Spacer(modifier = Modifier.height(20.dp))

        // Gambar Banner
        Image(
            painter = painterResource(id = R.drawable.ic_shopping_banner),
            contentDescription = "Login Banner",
            modifier = Modifier.height(200.dp)
        )

        // Spacer
        Spacer(modifier = Modifier.height(20.dp))

        // Input Email
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth()
        )

        // Spacer
        Spacer(modifier = Modifier.height(10.dp))

        // Input Full Name
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Spacer
        Spacer(modifier = Modifier.height(10.dp))

        // Input Password
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        // Spacer
        Spacer(modifier = Modifier.height(20.dp))

        // Tombol Sign Up
        Button(
            onClick = {
                // Set loading state
                isLoading.value = true
                
                // Panggil fungsi signUp dari ViewModel
                authViewModel.signUp(
                    email = email.value,
                    name = name.value,
                    password = password.value,
                    onResult = { isSuccess, errorMessage ->
                        // Reset loading state
                        isLoading.value = false
                        
                        if (!isSuccess) {
                            // Tampilkan pesan error jika gagal
                            AppUtil.showToast(context, errorMessage)
                        } else {
                            // Tampilkan pesan sukses
                            AppUtil.showToast(context, "Pendaftaran berhasil!")
                            
                            // Navigate ke Home screen dan clear back stack
                            navController.navigate("home") {
                                popUpTo("auth") {
                                    inclusive = true
                                }
                            }
                        }
                    }
                )
            },
            enabled = !isLoading.value,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(
                text = if (isLoading.value) "Creating Account..." else "Sign Up",
                fontSize = 18.sp
            )
        }
    }
}
