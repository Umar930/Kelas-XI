package com.umar.ecommerceapp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ProfilePage(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Judul
        Text(
            text = "Profile",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Informasi user
        val currentUser = Firebase.auth.currentUser
        Text(
            text = "Welcome!",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        currentUser?.email?.let { email ->
            Text(
                text = email,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Tombol Logout
        Button(
            onClick = {
                // Logout dari Firebase
                Firebase.auth.signOut()
                
                // Navigate ke auth screen dan clear back stack
                navController.navigate("auth") {
                    popUpTo("home") {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(text = "Logout", fontSize = 18.sp)
        }
    }
}
