package com.example.inventariosapp.domain.repository.sales

import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.PostSalesDao
import com.example.inventariosapp.model.error.ErrorModel
import com.example.inventariosapp.model.sales.GetSalesByIdResponse
import com.google.gson.Gson
import javax.inject.Inject

class EditSaleRepositoryImp @Inject constructor(
    private val postSalesDao: PostSalesDao,
    private val apiService: ApiService
) {
    suspend operator fun invoke(sale: GetSalesByIdResponse, saleId: String, internetUse: Boolean): Pair<Boolean?, String?> {
        if (internetUse){
            val r = apiService.editSale(sale, saleId)
            val response = try {
                if (r.isSuccessful){ Pair(true, null) }
                else {
                    var error: ErrorModel
                    val errorMsj = r.errorBody()?.string()
                    error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                    Pair(null, error.MsgError?.errors.toString())
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
            MainActivity.mainDialogMsg.value = "No hay conexión a internet"
            MainActivity.mainDialog.value = true
            return Pair(null, "No hay conexión a internet")
        }
    }
}