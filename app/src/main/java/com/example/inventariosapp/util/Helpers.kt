package com.example.inventariosapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlin.math.round

class Helpers {
    companion object {
        // region Date
        fun getDate(): String {
            val now = java.time.LocalDateTime.now()
            val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val date = now.format(formatter)
            return date.toString()
        }
        fun getTomrrow(): String {
            val now = java.time.LocalDateTime.now()
            val tomorrow = now.plusDays(1)
            val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val date = tomorrow.format(formatter)
            return date
        }
        fun getYesterday(): String {
            val now = java.time.LocalDateTime.now()
            val yesterday = now.minusDays(1)
            val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val date = yesterday.format(formatter)
            return date
        }
        fun getToday(): String {
            val now = java.time.LocalDateTime.now()
            val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val date = now.format(formatter)
            return date
        }
        fun get6Months(): String{
            val now = java.time.LocalDateTime.now()
            val yesterday = now.minusMonths(6)
            val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val date = yesterday.format(formatter)
            return date
        }
        fun getDateTime(): String {
            val now = java.time.LocalDateTime.now()
            val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")  //2026-01-2703:30:42 2025-09-11T06:00:00Z
            return now.format(formatter)
        }
        // endregion
        // region Internet
        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
        // endregion
        // region SharedPreferences
        val Context.dataStore by preferencesDataStore("app_prefs")
        suspend fun Context.savePersistData(data: Any, key: String){
            dataStore.edit { prefs ->
                when(data){
                    is Int -> prefs[intPreferencesKey(key)] = data as Int
                    is String -> prefs[stringPreferencesKey(key)] = data as String
                    is Double -> prefs[doublePreferencesKey(key)] = data as Double
                    is Boolean -> prefs[booleanPreferencesKey(key)] = data as Boolean
                    is Float -> prefs[floatPreferencesKey(key)] = data as Float
                    is Long -> prefs[longPreferencesKey(key)] = data as Long
                    else -> throw IllegalArgumentException("Unsupported data type: ${data::class.simpleName}")
                }
            }
        }
        suspend inline fun <reified T> Context.readPersistData(key: String, default: T): T {
            val prefs = dataStore.data.first()
            return when (T::class) {
                Int::class -> prefs[intPreferencesKey(key)] as? T ?: default
                String::class -> prefs[stringPreferencesKey(key)] as? T ?: default
                Double::class -> prefs[doublePreferencesKey(key)] as? T ?: default
                Boolean::class -> prefs[booleanPreferencesKey(key)] as? T ?: default
                Float::class -> prefs[floatPreferencesKey(key)] as? T ?: default
                Long::class -> prefs[longPreferencesKey(key)] as? T ?: default
                else -> default
            }
        }
        suspend fun Context.deletePersistKey(key: String) {
            withContext(Dispatchers.IO) {
                dataStore.edit { prefs ->
                    prefs.remove(stringPreferencesKey(key))
                    prefs.remove(intPreferencesKey(key))
                    prefs.remove(doublePreferencesKey(key))
                    prefs.remove(booleanPreferencesKey(key))
                    prefs.remove(floatPreferencesKey(key))
                    prefs.remove(longPreferencesKey(key))
                }
            }
        }
        suspend fun Context.clearDataStore() {
            dataStore.edit { prefs ->
                prefs.clear()
            }
        }
        // endregion
        // region formato
        fun Double.format2(): Double = round(this * 100) / 100
        // endregion
    }
}

