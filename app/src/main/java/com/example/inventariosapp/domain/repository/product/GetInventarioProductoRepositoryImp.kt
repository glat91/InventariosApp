package com.example.inventariosapp.domain.repository.product

import android.content.Context
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.model.error.ErrorModel
import com.example.inventariosapp.model.product.ProductIdResponseModel
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetInventarioProductoRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext var cnx: Context,
) {
    suspend operator fun invoke(productId: Int): Pair<ProductIdResponseModel?, ErrorModel?> {
        val r = apiService.getProductId(productId)
        val response = try {
            if (r.isSuccessful) { Pair(r.body(), null) }
            else {
                var error: ErrorModel
                val errorMsj = r.errorBody()?.string()
                error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                Pair(null, error)
            }
        } catch (e: Exception) {
            MainActivity.mainDialogMsg.value = e.toString()
            Pair(null, null)
        }
        return response
    }
}

