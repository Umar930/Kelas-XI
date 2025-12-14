package com.umar.realtimeweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umar.realtimeweather.api.Constants
import com.umar.realtimeweather.api.NetworkResponse
import com.umar.realtimeweather.api.RetrofitInstance
import com.umar.realtimeweather.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult
    
    fun getData(city: String) {
        _weatherResult.value = NetworkResponse.Loading
        
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.weatherAPI.getWeather(Constants.API_KEY, city)
                
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    } ?: run {
                        _weatherResult.value = NetworkResponse.Error("Data cuaca tidak ditemukan")
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Gagal memuat data: ${response.message()}")
                }
            } catch (e: Exception) {
                _weatherResult.value = NetworkResponse.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }
}
