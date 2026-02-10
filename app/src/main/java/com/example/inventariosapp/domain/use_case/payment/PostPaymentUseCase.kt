package com.example.inventariosapp.domain.use_case.payment

import com.example.appgeneric.model.payment.NewPayModel
import com.example.inventariosapp.domain.repository.payment.PostPaymentRepositoryImp
import javax.inject.Inject

class PostPaymentUseCase @Inject constructor(
    private val postPaymentUseCase: PostPaymentRepositoryImp,
) {
    suspend operator fun invoke(internetUse: Boolean, newPay: List<NewPayModel>): Result<Unit>{
        return postPaymentUseCase(internetUse, newPay)
    }
}