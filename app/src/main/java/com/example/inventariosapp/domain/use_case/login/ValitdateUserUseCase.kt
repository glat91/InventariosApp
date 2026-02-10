package com.example.inventariosapp.domain.use_case.login

import com.example.inventariosapp.domain.repository.login.ValitdateUserRepositoryImp
import com.example.inventariosapp.model.login.LoginResponseModel
import javax.inject.Inject

class ValitdateUserUseCase @Inject constructor(
    private val valitdateUserRepositoryImp: ValitdateUserRepositoryImp
) {
    suspend operator fun invoke(user: String, password: String): Pair<LoginResponseModel?, String?> {
        return valitdateUserRepositoryImp(user, password)
    }
}