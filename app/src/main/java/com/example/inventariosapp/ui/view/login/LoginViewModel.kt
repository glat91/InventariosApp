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

@HiltViewModel
class LoginViewModel @Inject constructor(
    val valitdaeUserUseCase: ValitdateUserUseCase,
    val baseViewModel: BaseViewModel,
    @ApplicationContext val cnx: Context
) : ViewModel() {
    // region User Login
    val user = mutableStateOf("")
    val password = mutableStateOf("")
    val rememberUser = mutableStateOf(false)
    val serverValidateUser = MutableStateFlow(false)
    fun validateUserLogin(internetUse: Boolean){
        baseViewModel.showLoader()
        viewModelScope.launch {
            if (internetUse){
                val v = valitdaeUserUseCase(user.value, password.value)
                if (v.first != null){
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

                    val a = cnx.readPersistData(Constants.PERFIL_ID, 0)
                    val b = cnx.readPersistData(Constants.USUARIO_ID, 0)
                    val c = cnx.readPersistData(Constants.USUARIO_SESION_ID, "")
                    val d = cnx.readPersistData(Constants.MAIL, "")
                    val e = cnx.readPersistData(Constants.NOMBRE, "")
                    Log.i("PErsist___", "$a $b $c $d $e")

                    baseViewModel.startSession(
                        perfilID,
                        usuarioID,
                        usuarioSesionID,
                        correo,
                        nombre
                    )
                    delay(4000)
                    serverValidateUser.value = true
                    baseViewModel.dialogLogin.value = false
                }
                else {
                    if (v.second != null) {
                        MainActivity.mainDialogMsg.value = v.second!!
                        MainActivity.mainDialog.value = true
                    }
                }
            }
            else{
                serverValidateUser.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    // endregion
    fun saveUserLogin(){
        viewModelScope.launch {
            cnx.savePersistData(key = Constants.REMEMBER_PASSWORD, data = "${user.value}/${password.value}")
        }
    }
    fun clearUser(){
        viewModelScope.launch {
            cnx.deletePersistKey(Constants.REMEMBER_PASSWORD)
        }
    }
    fun saveBoolean(key: String, data: Boolean){
        viewModelScope.launch {
            cnx.savePersistData(key = key, data = data)
        }
    }

    init {
        viewModelScope.launch {
            baseViewModel.showLoader()
            val session = baseViewModel.isSessionValid()
            if (session){
                serverValidateUser.value = true
            }
            else{
                val data = cnx.readPersistData(key = Constants.REMEMBER_PASSWORD, default = "")
                if (!data.isBlank()){
                    val s = data.split("/")
                    user.value = s[0]
                    password.value = s[1]
                    rememberUser.value = true
                }
            }
            baseViewModel.hideLoader()
        }
    }
}