package com.umar.realtimeweather.api

import com.google.gson.annotations.SerializedName

data class WeatherModel(
    @SerializedName("location")
    val location: Location,
    @SerializedName("current")
    val current: Current
)
