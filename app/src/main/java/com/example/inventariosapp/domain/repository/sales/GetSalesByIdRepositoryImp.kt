package com.example.inventariosapp.domain.repository.sales

import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.model.error.ErrorModel
import com.example.inventariosapp.model.sales.GetSalesByIdResponse
import com.google.gson.Gson
import java.io.IOException
import javax.inject.Inject

class GetSalesByIdRepositoryImp @Inject constructor(
    private val apiService: ApiService,
){
    suspend operator fun invoke(salesId: String): Pair<GetSalesByIdResponse?, ErrorModel?> {
        return try {
            val r = apiService.getSalesById(salesId)
            if (r.isSuccessful) {
                Pair(r.body(), null)
            }
            else {
                val errorMsj = r.errorBody()?.string()
                val error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                Pair(null, error)
            }
        }
        catch (e: IOException) {
             Pair(null, ErrorModel(null)) // Or a specific error for connectivity
        }
        catch (e: Exception){
            MainActivity.mainDialogMsg.value = e.toString()
            Pair(null, null)
        }
    }
}