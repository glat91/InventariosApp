package com.example.inventariosapp.domain.repository.payment

import android.content.Context
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.domain.model.error.ErrorModel
import com.example.inventariosapp.domain.model.sales.GetPaymentResponseModel
import com.example.inventariosapp.util.Helpers
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetPaymentMethodRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext val cnx: Context
) {
    suspend operator fun invoke(): Pair<List<GetPaymentResponseModel>?, ErrorModel?> {
        if (Helpers.isInternetAvailable(cnx)){
            val r = apiService.getPaymentMethod()
            val response = try {
                if (r.isSuccessful) { Pair(r.body(), null) }
                else {
                    var error: ErrorModel
                    val errorMsj = r.errorBody()?.string()
                    error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                    Pair(null, error)
                }
            }
            catch (e: Exception){ Pair(null, null) }
            return response
        }
        else { return Pair(listOf(GetPaymentResponseModel()), null) }
    }
}