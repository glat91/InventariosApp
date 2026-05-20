package com.example.inventariosapp.domain.use_case.sales

import com.example.inventariosapp.domain.repository.sales.GetPendingSalesRepositoryImp
import com.example.inventariosapp.domain.model.sales.SalesModel
import javax.inject.Inject

class GetPendingSalesUseCase @Inject constructor(
    private val getPendingSalesRepositoryImp: GetPendingSalesRepositoryImp
){
    suspend operator fun invoke(estatusVentaIds: String, startDate: String, endDate: String, refresh: Boolean): Pair<List<SalesModel>?, String?>{
        return getPendingSalesRepositoryImp(estatusVentaIds, startDate, endDate, refresh)
    }
}