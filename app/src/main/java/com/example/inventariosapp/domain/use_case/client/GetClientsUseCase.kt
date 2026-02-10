package com.example.inventariosapp.domain.use_case.client

import com.example.inventariosapp.domain.repository.client.GetClientsRepositoryImp
import com.example.inventariosapp.model.client.ClientResponseModel
import javax.inject.Inject

class GetClientsUseCase@Inject constructor(
    private val getClientsRepositoryImp: GetClientsRepositoryImp
) {
    suspend operator fun invoke(refresh: Boolean): Pair<List<ClientResponseModel>?, String?> {
        return getClientsRepositoryImp(refresh)
    }
}