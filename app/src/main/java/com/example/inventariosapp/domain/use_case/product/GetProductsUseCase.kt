package com.example.inventariosapp.domain.use_case.product

import com.example.inventariosapp.domain.repository.product.GetProductsRepositoryImp
import com.example.inventariosapp.domain.model.product.ProductsResponseModel
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val getProductsRepositoryImp: GetProductsRepositoryImp
) {
    suspend operator fun invoke(internetUse: Boolean): Pair<List<ProductsResponseModel>?, String?>{
        return getProductsRepositoryImp(internetUse)

    }
}