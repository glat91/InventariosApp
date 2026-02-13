package com.example.inventariosapp.domain.use_case.sales

import com.example.inventariosapp.domain.repository.sales.GetSalesInProcessRepositoryImp
import com.example.inventariosapp.domain.model.sales.SalesModel
import javax.inject.Inject

class GetSalesInProcessUseCase @Inject constructor(
    private val getSalesInProcessRepositoryImp: GetSalesInProcessRepositoryImp,
) {
    suspend operator fun invoke(startDate: String, endDate: String, refresh: Boolean): Pair<List<SalesModel>?, String?> {
        return getSalesInProcessRepositoryImp(startDate, endDate, refresh)
    }
}