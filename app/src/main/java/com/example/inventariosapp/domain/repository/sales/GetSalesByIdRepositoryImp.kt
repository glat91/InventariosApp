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
    suspend operator fun invoke(salesId: String, internetUse: Boolean): Pair<GetSalesByIdResponse?, String?> {
        if (internetUse){
            return try {
                val r = apiService.getSalesById(salesId)
                if (r.isSuccessful) {
                    Pair(r.body(), null)
                }
                else {
                    val errorMsj = r.errorBody()?.string()
                    val error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                    MainActivity.mainDialogMsg.value = error.MsgError?.errors.toString()
                    MainActivity.mainDialog.value = true
                    Pair(null, error.MsgError?.errors.toString())
                }
            }
            catch (e: IOException) {
                MainActivity.mainDialogMsg.value = e.message.toString()
                MainActivity.mainDialog.value = true
                Pair(null, e.message.toString())
            }
            catch (e: Exception){
                MainActivity.mainDialogMsg.value = e.toString()
                MainActivity.mainDialog.value = true
                Pair(null, null)
            }
        }
        else{
            return Pair(null, "Modo offline")
        }
    }
}