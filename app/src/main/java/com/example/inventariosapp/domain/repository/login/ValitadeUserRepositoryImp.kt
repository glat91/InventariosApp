package com.example.inventariosapp.domain.repository.login

import android.os.Build
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.domain.model.login.LoginRequest
import com.example.inventariosapp.domain.model.login.LoginResponseModel
import com.google.gson.Gson
import java.io.IOException
import javax.inject.Inject

class ValitdateUserRepositoryImp @Inject constructor(
    private val apiService: ApiService,
) {
    suspend operator fun invoke(user: String, password: String): Pair<LoginResponseModel?, String?> {
        return try {
            val request = LoginRequest(
                LoginName = user,
                Contrasenia = password,
                Manufacturer = Build.MANUFACTURER,
                Brand = Build.BRAND,
                Model = Build.MODEL,
                Sdk = Build.VERSION.SDK_INT.toString(),
                AndroidVersion = Build.VERSION.RELEASE
            )
            val response = apiService.validateUser(user, password)

            if (response.isSuccessful) { Pair(response.body(), null) }
            else {
                val errorBody = response.errorBody()?.string()
                val error = try { Gson().fromJson(errorBody, String::class.java) }
                catch (e: Exception) { "Credenciales incorrectas" }

                Pair(null, error)
            }

        }
        catch (e: IOException) { Pair(null, "No hay conexión a internet") }
        catch (e: Exception) { Pair(null, ("Ocurrió un error inesperado ${e.message}")) }
    }
}