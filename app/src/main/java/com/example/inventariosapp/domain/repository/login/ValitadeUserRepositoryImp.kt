package com.example.inventariosapp.domain.repository.login

import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.model.login.LoginResponseModel
import com.google.gson.Gson
import java.io.IOException
import javax.inject.Inject

class ValitdaeUserRepositoryImp @Inject constructor(
    private val apiService: ApiService,
) {
    suspend operator fun invoke(user: String, password: String): Pair<LoginResponseModel?, String?> {
        return try {
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
        catch (e: Exception) { Pair(null, ("Ocurrió un error inesperado")) }
    }
}