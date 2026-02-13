package com.example.inventariosapp.session

import android.content.Context
import android.os.SystemClock
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.inventariosapp.util.Constants
import com.example.inventariosapp.util.Helpers.Companion.readPersistData
import com.example.inventariosapp.util.Helpers.Companion.savePersistData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val cnx: Context
) {
    companion object {
        private const val KEY_USER_ID = "key_user_id"
        private const val KEY_LOGIN_ELAPSED = "key_login_elapsed"
        private const val KEY_LOGIN_WALL = "key_login_wall"
        private const val KEY_SESSION_ACTIVE = "key_session_active"
        private const val SESSION_DURATION = 8 * 60 * 60 * 1000L
    }
    val dialogLogin = mutableStateOf(false)

    suspend fun startSession(
        perfilId: Int,
        usuarioId: Int,
        usuarioSesionId: Int,
        correo: String,
        nombre: String,
    ) {
        val elapsedTime = SystemClock.elapsedRealtime()
        val wallTime = System.currentTimeMillis()
        // session mannager
        cnx.savePersistData(usuarioId, KEY_USER_ID)
        cnx.savePersistData(elapsedTime, KEY_LOGIN_ELAPSED)
        cnx.savePersistData(wallTime, KEY_LOGIN_WALL)
        cnx.savePersistData(true, KEY_SESSION_ACTIVE)
        // user backend data
        cnx.savePersistData(perfilId, Constants.PERFIL_ID)
        cnx.savePersistData(usuarioId, Constants.USUARIO_ID)
        cnx.savePersistData(usuarioSesionId, Constants.USUARIO_SESION_ID)
        cnx.savePersistData(correo, Constants.MAIL)
        cnx.savePersistData(nombre, Constants.NOMBRE)

        val a = cnx.readPersistData(Constants.PERFIL_ID, 0)
        val b = cnx.readPersistData(Constants.USUARIO_ID, 0)
        val c = cnx.readPersistData(Constants.USUARIO_SESION_ID, 0)
        val d = cnx.readPersistData(Constants.MAIL, "")
        val e = cnx.readPersistData(Constants.NOMBRE, "")
        Log.i("Session___", "$a $b $c $d $e")
    }

    suspend fun isSessionValid(): Boolean {
        val isActive = cnx.readPersistData(KEY_SESSION_ACTIVE, false)
        if (!isActive) return false

        val loginElapsed = cnx.readPersistData(KEY_LOGIN_ELAPSED, 0L)
        val currentElapsed = SystemClock.elapsedRealtime()

        val diff = currentElapsed - loginElapsed
        val isSessionActive = diff <= SESSION_DURATION
        Log.i("SessionActive___","${isSessionActive}")
        return diff <= SESSION_DURATION
    }

    suspend fun getRemainingTime(): Long {
        val loginElapsed = cnx.readPersistData(KEY_LOGIN_ELAPSED, 0L)
        val currentElapsed = SystemClock.elapsedRealtime()
        val diff = currentElapsed - loginElapsed

        return (SESSION_DURATION - diff).coerceAtLeast(0L)
    }

    suspend fun getPerfilId() = cnx.readPersistData(Constants.PERFIL_ID, 0)
    suspend fun getUsiarioId(): Int {
        val userId = cnx.readPersistData(Constants.USUARIO_ID, 0)
        Log.i("User_ID___", userId.toString())
        return userId
    }
    suspend fun getUsuarioSessionId() = cnx.readPersistData(Constants.USUARIO_SESION_ID, 0)
    suspend fun getMail() = cnx.readPersistData(Constants.MAIL, "")
    suspend fun getGetName() = cnx.readPersistData(Constants.NOMBRE, "")
    suspend fun logout() {
        cnx.savePersistData(false, KEY_SESSION_ACTIVE)
        cnx.savePersistData("", KEY_USER_ID)
        cnx.savePersistData(0L, KEY_LOGIN_ELAPSED)
        cnx.savePersistData(0L, KEY_LOGIN_WALL)

        cnx.savePersistData(0, Constants.PERFIL_ID)
        cnx.savePersistData(0, Constants.USUARIO_ID)
        cnx.savePersistData("", Constants.USUARIO_SESION_ID)
        cnx.savePersistData("", Constants.MAIL)
        cnx.savePersistData("", Constants.NOMBRE)
    }
}