package com.example.inventariosapp.domain.use_case.payment

import com.example.inventariosapp.domain.repository.payment.GetPaymentRepositoryImp
import com.example.inventariosapp.model.payment.PayModel
import javax.inject.Inject

class GetPaymentUseCase @Inject constructor(
    private val getPaymentUseCase: GetPaymentRepositoryImp
) {
    suspend operator fun invoke(ventaID: String, internetUse: Boolean): Pair<List<PayModel>?, String?> {
        return getPaymentUseCase.getPayments(internetUse = internetUse, ventaID = ventaID)
    }
}