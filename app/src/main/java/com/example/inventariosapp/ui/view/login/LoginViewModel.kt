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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val valitdaeUserUseCase: ValitdateUserUseCase,
    @ApplicationContext val cnx: Context
) : ViewModel() {
    val baseViewModel = BaseViewModel()
    // region User Login
    val user = mutableStateOf("")
    val password = mutableStateOf("")
    val rememberUser = mutableStateOf(false)
    val serverValidateUser = MutableStateFlow(false)
    fun validateUserLogin(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val internetUse = Helpers.isInternetAvailable(cnx)
            if (internetUse){
                val v = valitdaeUserUseCase(user.value, password.value)
                if (v.first != null){
                    cnx.savePersistData(v.first!!.perfilId!!, Constants.PERFIL_ID)
                    cnx.savePersistData(v.first!!.usuarioId!!, Constants.USUARIO_ID)
                    cnx.savePersistData(v.first!!.usuarioSesionId!!, Constants.USUARIO_SESION_ID)
                    cnx.savePersistData(v.first!!.correo!!, Constants.MAIL)
                    cnx.savePersistData(v.first!!.nombre!!, Constants.NOMBRE)

                    val a = cnx.readPersistData(Constants.PERFIL_ID, 0)
                    val b = cnx.readPersistData(Constants.USUARIO_ID, 0)
                    val c = cnx.readPersistData(Constants.USUARIO_SESION_ID, "")
                    val d = cnx.readPersistData(Constants.MAIL, "")
                    val e = cnx.readPersistData(Constants.NOMBRE, "")
                    Log.i("PErsist___", "$a $b $c $d $e")
                    serverValidateUser.value = true

                }
                else {
                    if (v.second != null) {
                        MainActivity.mainDialogMsg.value = v!!.second!!
                    }
                    else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                    MainActivity.mainDialog.value = true
                }
            }
            else{
                MainActivity.mainDialogMsg.value = "Favor de iniciar session antes de Editar o Subir Pagos y Ventas"
                MainActivity.mainDialog.value = true
                serverValidateUser.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    // endregion
    fun saveUserLogin(cnx: Context){
        viewModelScope.launch {
            cnx.savePersistData(key = Constants.REMEMBER_PASSWORD, data = "${user.value}/${password.value}")
        }
    }
    fun clearUser(cnx: Context){
        viewModelScope.launch {
            cnx.deletePersistKey(Constants.REMEMBER_PASSWORD)
        }
    }

    init {
        viewModelScope.launch {
            val data = cnx.readPersistData(key = Constants.REMEMBER_PASSWORD, default = "")
            if (!data.isBlank()){
                val s = data.split("/")
                user.value = s[0]
                password.value = s[1]
                rememberUser.value = true
            }
        }
    }
}