package com.example.inventariosapp.domain.use_case.sales

import com.example.inventariosapp.domain.repository.sales.PostSaleRepositoryImp
import com.example.inventariosapp.model.sales.PostSalesModel
import javax.inject.Inject

class PostSaleUseCase @Inject constructor(
    private val postSaleRepositoryImp: PostSaleRepositoryImp,
) {
    suspend operator fun invoke(sales: List<PostSalesModel>?, updateSales: Boolean): Pair<Unit?, String?> {
        return postSaleRepositoryImp(sales, updateSales)
    }
}