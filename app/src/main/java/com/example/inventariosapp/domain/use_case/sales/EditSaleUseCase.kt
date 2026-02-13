package com.example.inventariosapp.domain.use_case.sales

import com.example.inventariosapp.domain.repository.sales.EditSaleRepositoryImp
import com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse
import javax.inject.Inject

class EditSaleUseCase @Inject constructor(
    private val editSaleRepositoryImp: EditSaleRepositoryImp,
) {
    suspend operator fun invoke(sale: GetSalesByIdResponse, saleId: String, internetUse: Boolean): Pair<Boolean?, String?> {
        return editSaleRepositoryImp(sale, saleId, internetUse)
    }
}