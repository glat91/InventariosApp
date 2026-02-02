package com.example.inventariosapp.domain.repository.sales

import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.model.error.ErrorModel
import com.example.inventariosapp.model.sales.GetSalesByIdResponse
import com.google.gson.Gson
import javax.inject.Inject

class GetSalesByIdRepositoryImp @Inject constructor(
    private val apiService: ApiService,
){
    suspend operator fun invoke(salesId: String): Pair<GetSalesByIdResponse?, ErrorModel?> {
        val r = apiService.getSalesById(salesId)
        val response = try {
            if (r.isSuccessful) {
                Pair(r.body(), null)
            }
            else {
                var error: ErrorModel
                val errorMsj = r.errorBody()?.string()
                error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                Pair(null, error)
            }
        }
        catch (e: Exception){
            MainActivity.mainDialogMsg.value = e.toString()
            Pair(null, null)
        }
        return response
    }
}