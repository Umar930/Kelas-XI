package com.umar.animation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umar.animation.R
import kotlinx.coroutines.launch

@Composable
fun SimpleAnimationPage() {
    // Variabel kontrol untuk memicu animasi ulang
    var animateAgain by remember { mutableStateOf(false) }
    
    // Variabel animasi skala dengan nilai awal 0f
    val scale = remember { Animatable(0f) }
    
    // Coroutine scope untuk mengelola animasi saat button diklik
    val scope = rememberCoroutineScope()
    
    // LaunchedEffect untuk menjalankan animasi saat komposisi pertama kali atau ketika animateAgain berubah
    LaunchedEffect(key1 = animateAgain) {
        // Animasi skala dari 0f ke 1f dengan durasi 2 detik dan efek "back" yang memberikan efek overshoot
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 2000,
                easing = EaseInOutBack
            )
        )
    }
    
    // Column sebagai container utama
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gambar dengan animasi skala menggunakan gambar Captain America
        Image(
            painter = painterResource(id = R.drawable.captain_america),
            contentDescription = "Captain America",
            modifier = Modifier
                .size(200.dp)
                .weight(1f)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
        )
        
        // Tombol untuk memulai animasi ulang
        Button(
            onClick = {
                scope.launch {
                    // Reset skala ke 0f secara instan
                    scale.snapTo(0f)
                    // Balikkan nilai animateAgain untuk memicu ulang LaunchedEffect
                    animateAgain = !animateAgain
                }
            }
        ) {
            Text("Animate")
        }
    }
}