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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.umar.ecommerceapp.R

@Composable
fun AuthScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gambar Banner
        Image(
            painter = painterResource(id = R.drawable.ic_shopping_banner),
            contentDescription = "Shopping Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        // Spacer
        Spacer(modifier = Modifier.height(20.dp))

        // Judul Utama
        Text(
            text = "Mulai Perjalanan Belanja Anda Sekarang",
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        // Spacer
        Spacer(modifier = Modifier.height(10.dp))

        // Slogan
        Text(
            text = "Platform E-commerce Terbaik dengan Harga Terbaik",
            textAlign = TextAlign.Center
        )

        // Spacer
        Spacer(modifier = Modifier.height(20.dp))

        // Tombol Login
        Button(
            onClick = {
                navController.navigate("login")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(text = "Login", fontSize = 18.sp)
        }

        // Spacer
        Spacer(modifier = Modifier.height(20.dp))

        // Tombol Sign Up
        OutlinedButton(
            onClick = {
                navController.navigate("signup")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(text = "Daftar", fontSize = 18.sp)
        }
    }
}
