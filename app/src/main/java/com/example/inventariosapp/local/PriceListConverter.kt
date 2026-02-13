package com.example.inventariosapp.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PriceListConverter {
    @TypeConverter
    fun fromDoubleList(list: List<Double>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toDoubleList(json: String?): List<Double> {
        if (json.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<Double>>() {}.type
        return Gson().fromJson(json, type)
    }
}