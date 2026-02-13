package com.example.inventariosapp.domain.repository.payment

import com.example.appgeneric.model.payment.NewPayModel
import com.example.inventariosapp.local.dao.NewPayDao
import com.example.inventariosapp.local.entity.toModel
import javax.inject.Inject

class GetPenndingPaymentRepositoryImp @Inject constructor(
    private val newPayDao: NewPayDao,
) {
    suspend operator fun invoke(): List<NewPayModel> = newPayDao.getAll().map { it.toModel() }
}