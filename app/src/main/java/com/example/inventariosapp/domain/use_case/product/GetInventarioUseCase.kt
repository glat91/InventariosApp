package com.example.inventariosapp.domain.use_case.product

import com.example.inventariosapp.domain.model.error.ErrorModel
import com.example.inventariosapp.domain.model.product.InventarioRseponeModel
import com.example.inventariosapp.domain.repository.product.GetInventarioRepositoryImp
import javax.inject.Inject

class GetInventarioUseCase @Inject constructor(
    private val getInventarioRepositoryImp: GetInventarioRepositoryImp
){
    suspend operator fun invoke(): Pair<List<InventarioRseponeModel>?, ErrorModel?>{
        return getInventarioRepositoryImp()
    }
}