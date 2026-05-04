package com.example.inventariosapp.ui.view.login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.domain.use_case.login.ValitdateUserUseCase
import com.example.inventariosapp.util.Constants
import com.example.inventariosapp.util.Helpers
import com.example.inventariosapp.util.Helpers.Companion.deletePersistKey
import com.example.inventariosapp.util.Helpers.Companion.readPersistData
import com.example.inventariosapp.util.Helpers.Companion.savePersistData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.Immutable


data class LoginUiState(
    val user: String = "",
    val password: String = "",
    val rememberUser: Boolean = false,
    val serverValidateUser: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    val valitdaeUserUseCase: ValitdateUserUseCase,
    val baseViewModel: BaseViewModel,
    @ApplicationContext val cnx: Context
) : ViewModel() {
    // region UI State
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: MutableStateFlow<LoginUiState> get() = _uiState
    fun updateUser(newUser: String) {
        _uiState.value = _uiState.value.copy(user = newUser)
    }
    fun updatePassword(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }
    fun toggleRememberUser() {
        _uiState.value = _uiState.value.copy(rememberUser = !_uiState.value.rememberUser)
    }
    // endregion
    fun validateUserLogin(internetUse: Boolean) {
        baseViewModel.showLoader()
        viewModelScope.launch {
            if (internetUse) {
                val v = valitdaeUserUseCase(uiState.value.user, uiState.value.password)
                if (v.first != null) {
                    val perfilID = v.first!!.perfilId!!
                    val usuarioID = v.first!!.usuarioId!!
                    val usuarioSesionID = v.first!!.usuarioSesionId!!
                    val correo = v.first!!.correo.toString()
                    val nombre = v.first!!.nombre.toString()

                    cnx.savePersistData(perfilID, Constants.PERFIL_ID)
                    cnx.savePersistData(usuarioID, Constants.USUARIO_ID)
                    cnx.savePersistData(usuarioSesionID, Constants.USUARIO_SESION_ID)
                    cnx.savePersistData(correo, Constants.MAIL)
                    cnx.savePersistData(nombre, Constants.NOMBRE)

                    baseViewModel.setUsarId(cnx.readPersistData(Constants.PERFIL_ID, 0))
                    val b = cnx.readPersistData(Constants.USUARIO_ID, 0)
                    val c = cnx.readPersistData(Constants.USUARIO_SESION_ID, "")
                    val d = cnx.readPersistData(Constants.MAIL, "")
                    val e = cnx.readPersistData(Constants.NOMBRE, "")
                    Log.i("Persist___", "${baseViewModel.getPerfilId()} $b $c $d $e")

                    baseViewModel.startSession(
                        perfilID,
                        usuarioID,
                        usuarioSesionID,
                        correo,
                        nombre
                    )
                    delay(4000)
                    _uiState.value = _uiState.value.copy(serverValidateUser = true)
                    baseViewModel.dialogLogin.value = false
                } else {
                    if (v.second != null) {
                        MainActivity.mainDialogMsg.value = v.second!!
                        MainActivity.mainDialog.value = true
                    }
                }
            } else {
                _uiState.value = _uiState.value.copy(serverValidateUser = true)
            }
            baseViewModel.hideLoader()
        }
    }
    fun saveUserLogin() {
        viewModelScope.launch {
            cnx.savePersistData(key = Constants.REMEMBER_PASSWORD, data = "${uiState.value.user}/${uiState.value.password}")
        }
    }
    fun clearUser() {
        viewModelScope.launch {
            cnx.deletePersistKey(Constants.REMEMBER_PASSWORD)
        }
    }
    fun saveBoolean(key: String, data: Boolean) {
        viewModelScope.launch {
            cnx.savePersistData(key = key, data = data)
        }
    }
    init {
        viewModelScope.launch {
            baseViewModel.showLoader()
            val session = baseViewModel.isSessionValid()
            if (session) {
                _uiState.value = _uiState.value.copy(serverValidateUser = true)
            } else {
                val data = cnx.readPersistData(key = Constants.REMEMBER_PASSWORD, default = "")
                if (!data.isBlank()) {
                    val s = data.split("/")
                    _uiState.value = _uiState.value.copy(user = s[0], password = s[1], rememberUser = true)
                }
            }
            baseViewModel.hideLoader()
        }
    }
}