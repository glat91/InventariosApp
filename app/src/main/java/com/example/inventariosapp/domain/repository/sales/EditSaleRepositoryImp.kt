package com.example.inventariosapp.domain.repository.sales

import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.model.error.ErrorModel
import com.example.inventariosapp.model.sales.GetSalesByIdResponse
import com.google.gson.Gson
import javax.inject.Inject

class EditSaleRepositoryImp @Inject constructor(
    private val apiService: ApiService
) {
    suspend operator fun invoke(sale: GetSalesByIdResponse, saleId: String): Pair<Boolean?, ErrorModel?> {
        val r = apiService.editSale(sale, saleId)
        val response = try {
            if (r.isSuccessful){ Pair(true, null) }
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