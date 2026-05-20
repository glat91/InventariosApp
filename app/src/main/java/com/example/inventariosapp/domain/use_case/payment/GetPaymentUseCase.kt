package com.example.inventariosapp.domain.use_case.payment

import com.example.inventariosapp.domain.repository.payment.GetPaymentRepositoryImp
import com.example.inventariosapp.domain.model.payment.PayModel
import javax.inject.Inject

class GetPaymentUseCase @Inject constructor(
    private val getPaymentRepositoryImp: GetPaymentRepositoryImp
) {
    suspend operator fun invoke(ventaID: String, internetUse: Boolean): Pair<List<PayModel>?, String?> {
        return getPaymentRepositoryImp(ventaID = ventaID)
    }
}