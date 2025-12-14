package com.umar.animation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.umar.animation.ui.SimpleAnimationPage
import com.umar.animation.ui.theme.AnimationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimationTheme {
                SimpleAnimationPage()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleAnimationPagePreview() {
    AnimationTheme {
        SimpleAnimationPage()
    }
}