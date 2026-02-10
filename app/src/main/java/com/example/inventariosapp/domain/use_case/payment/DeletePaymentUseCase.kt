package com.example.inventariosapp.domain.use_case.payment

import com.example.inventariosapp.domain.repository.payment.DeletePaymentRepositoryImp
import javax.inject.Inject

class DeletePaymentUseCase @Inject constructor(
    private val deletePaymentUseCase: DeletePaymentRepositoryImp,
) {
    suspend operator fun invoke(refresh: Boolean, pagoId: Int): Result<Unit>{
        return deletePaymentUseCase(refresh, pagoId)
    }
}