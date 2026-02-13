package com.example.inventariosapp.domain.use_case.sales

import com.example.inventariosapp.domain.repository.sales.GetSalesByIdRepositoryImp
import com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse
import javax.inject.Inject

class GetSalesByIdUseCase @Inject constructor(
    private val getSalesByIdRepositoryImp: GetSalesByIdRepositoryImp,
) {
    suspend operator fun invoke(salesId: String, internetUse: Boolean): Pair<GetSalesByIdResponse?, String?> {
        return getSalesByIdRepositoryImp(salesId, internetUse)
    }
}