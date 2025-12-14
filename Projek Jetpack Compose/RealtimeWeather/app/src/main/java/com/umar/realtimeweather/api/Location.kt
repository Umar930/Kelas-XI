package com.umar.realtimeweather.api

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("name")
    val name: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lon")
    val lon: String,
    @SerializedName("tz_id")
    val tzId: String,
    @SerializedName("localtime")
    val localtime: String
)
