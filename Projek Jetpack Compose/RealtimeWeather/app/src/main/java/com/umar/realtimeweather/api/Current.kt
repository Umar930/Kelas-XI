package com.umar.realtimeweather.api

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("temp_c")
    val temp_c: String,
    @SerializedName("temp_f")
    val temp_f: String,
    @SerializedName("condition")
    val condition: Condition,
    @SerializedName("wind_mph")
    val wind_mph: String,
    @SerializedName("wind_kph")
    val wind_kph: String,
    @SerializedName("wind_degree")
    val wind_degree: String,
    @SerializedName("wind_dir")
    val wind_dir: String,
    @SerializedName("pressure_mb")
    val pressure_mb: String,
    @SerializedName("precip_mm")
    val precip_mm: String,
    @SerializedName("humidity")
    val humidity: String,
    @SerializedName("cloud")
    val cloud: String,
    @SerializedName("feelslike_c")
    val feelslike_c: String,
    @SerializedName("feelslike_f")
    val feelslike_f: String,
    @SerializedName("vis_km")
    val vis_km: String,
    @SerializedName("uv")
    val uv: String
)
