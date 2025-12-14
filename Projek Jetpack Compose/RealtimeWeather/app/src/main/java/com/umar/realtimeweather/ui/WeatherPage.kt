package com.umar.realtimeweather.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.umar.realtimeweather.api.NetworkResponse
import com.umar.realtimeweather.api.WeatherModel
import com.umar.realtimeweather.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherPage(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = viewModel()
) {
    var city by remember { mutableStateOf("") }
    val weatherResult = viewModel.weatherResult.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header - Input pencarian
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Masukkan nama kota") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            IconButton(
                onClick = {
                    if (city.isNotBlank()) {
                        viewModel.getData(city)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Cari",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Body - Menampilkan hasil
        when (val result = weatherResult.value) {
            is NetworkResponse.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is NetworkResponse.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = result.message,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                }
            }
            
            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }
            
            null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Masukkan nama kota untuk melihat cuaca",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherDetails(data: WeatherModel) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Lokasi
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Lokasi",
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF6200EE)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Column {
                Text(
                    text = data.location.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = data.location.country,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Suhu Utama
        Text(
            text = "${data.current.temp_c}°C",
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6200EE)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Ikon dan Kondisi Cuaca
        val iconUrl = "https:${data.current.condition.icon}".replace("64x64", "128x128")
        
        AsyncImage(
            model = iconUrl,
            contentDescription = "Ikon Cuaca",
            modifier = Modifier.size(128.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = data.current.condition.text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Detail Tambahan dalam Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Detail Cuaca",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                WeatherKeyValue("Kelembaban", "${data.current.humidity}%")
                Spacer(modifier = Modifier.height(12.dp))
                
                WeatherKeyValue("Kecepatan Angin", "${data.current.wind_kph} kph")
                Spacer(modifier = Modifier.height(12.dp))
                
                WeatherKeyValue("Arah Angin", data.current.wind_dir)
                Spacer(modifier = Modifier.height(12.dp))
                
                WeatherKeyValue("Terasa Seperti", "${data.current.feelslike_c}°C")
                Spacer(modifier = Modifier.height(12.dp))
                
                WeatherKeyValue("Tekanan Udara", "${data.current.pressure_mb} mb")
                Spacer(modifier = Modifier.height(12.dp))
                
                WeatherKeyValue("Jarak Pandang", "${data.current.vis_km} km")
                Spacer(modifier = Modifier.height(12.dp))
                
                WeatherKeyValue("UV Index", data.current.uv)
                Spacer(modifier = Modifier.height(12.dp))
                
                // Memisahkan tanggal dan waktu
                val dateTime = data.location.localtime.split(" ")
                val date = if (dateTime.isNotEmpty()) dateTime[0] else ""
                val time = if (dateTime.size > 1) dateTime[1] else ""
                
                WeatherKeyValue("Tanggal", date)
                Spacer(modifier = Modifier.height(12.dp))
                
                WeatherKeyValue("Waktu Lokal", time)
            }
        }
    }
}

@Composable
fun WeatherKeyValue(key: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = key,
            fontSize = 16.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
