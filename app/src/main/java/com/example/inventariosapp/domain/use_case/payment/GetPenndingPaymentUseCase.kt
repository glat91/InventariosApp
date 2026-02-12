package com.example.inventariosapp.domain.use_case.payment

import com.example.inventariosapp.domain.repository.payment.GetPenndingPaymentRepositoryImp
import javax.inject.Inject

class GetPenndingPaymentUseCase @Inject constructor(
    private val repository: GetPenndingPaymentRepositoryImp
) {
    suspend operator fun invoke() = repository()
}