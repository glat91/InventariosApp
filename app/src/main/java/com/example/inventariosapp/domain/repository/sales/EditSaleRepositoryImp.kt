package com.example.inventariosapp.domain.repository.sales

import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.domain.model.error.ErrorModel
import com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse
import com.example.inventariosapp.session.SessionManager
import com.google.gson.Gson
import javax.inject.Inject

class EditSaleRepositoryImp @Inject constructor(
    private val sessionManager: SessionManager,
    private val apiService: ApiService
) {
    suspend operator fun invoke(sale: GetSalesByIdResponse, saleId: String, internetUse: Boolean): Pair<Boolean?, String?> {
        if (internetUse){
            if (sessionManager.isSessionValid()) {
                val r = apiService.editSale(sale, saleId)
                val response = try {
                    if (r.isSuccessful){ Pair(true, null) }
                    else {
                        var error: ErrorModel
                        val errorMsj = r.errorBody()?.string()
                        error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                        Pair(null, "Error en la peticion favor de intentar mas tarde")
                    }
                }
                catch (e: Exception){
                    MainActivity.mainDialogMsg.value = e.toString()
                    MainActivity.mainDialog.value = true
                    Pair(null, null)
                }
                return response
            }
            else{
                MainActivity.mainDialogMsg.value ="Sesión expirada, favor de iniciar sesión"
                MainActivity.mainDialog.value = true
                return Pair(null, "Sesión expirada, favor de iniciar sesión")
            }
        }
        else{
            MainActivity.mainDialogMsg.value = "No hay conexión a internet"
            MainActivity.mainDialog.value = true
            return Pair(null, "No hay conexión a internet")
        }
    }
}