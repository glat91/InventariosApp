package com.example.inventariosapp.session

import android.content.Context
import android.os.SystemClock
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
        private const val KEY_SESSION_ACTIVE = "key_session_active"
        private const val SESSION_DURATION = 8 * 60 * 60 * 1000L
    }

    suspend fun startSession(
        perfilId: String,
        usuarioId: String,
        usuarioSesionId: String,
        correo: String,
        nombre: String,
    ) {
        val elapsedTime = SystemClock.elapsedRealtime()
        val wallTime = System.currentTimeMillis()
        // session mannager
        cnx.savePersistData(usuarioId, KEY_USER_ID)
        cnx.savePersistData(elapsedTime, KEY_LOGIN_ELAPSED)
        cnx.savePersistData(true, KEY_SESSION_ACTIVE)
        // user backend data
        cnx.savePersistData(perfilId, Constants.PERFIL_ID)
        cnx.savePersistData(usuarioId, Constants.USUARIO_ID)
        cnx.savePersistData(usuarioSesionId, Constants.USUARIO_SESION_ID)
        cnx.savePersistData(correo, Constants.MAIL)
        cnx.savePersistData(nombre, Constants.NOMBRE)
    }

    suspend fun isSessionValid(): Boolean {
        val isActive = cnx.readPersistData(KEY_SESSION_ACTIVE, false)
        if (!isActive) return false

        val loginElapsed = cnx.readPersistData(KEY_LOGIN_ELAPSED, 0L)
        val currentElapsed = SystemClock.elapsedRealtime()

        val diff = currentElapsed - loginElapsed

        return diff <= SESSION_DURATION
    }

    suspend fun getRemainingTime(): Long {
        val loginElapsed = cnx.readPersistData(KEY_LOGIN_ELAPSED, 0L)
        val currentElapsed = SystemClock.elapsedRealtime()
        val diff = currentElapsed - loginElapsed

        return (SESSION_DURATION - diff).coerceAtLeast(0L)
    }

    suspend fun getPerfilId() = cnx.readPersistData(Constants.PERFIL_ID, 0)
    suspend fun getUsiarioId() =cnx.readPersistData(Constants.USUARIO_ID, 0)
    suspend fun getUsuarioSessionId() = cnx.readPersistData(Constants.USUARIO_SESION_ID, "")
    suspend fun getMail() = cnx.readPersistData(Constants.MAIL, "")
    suspend fun getGetName() = cnx.readPersistData(Constants.NOMBRE, "")
    suspend fun logout() {
        cnx.savePersistData(false, KEY_SESSION_ACTIVE)
        cnx.savePersistData("", KEY_USER_ID)
        cnx.savePersistData(0L, KEY_LOGIN_ELAPSED)

        cnx.savePersistData(0, Constants.PERFIL_ID)
        cnx.savePersistData(0, Constants.USUARIO_ID)
        cnx.savePersistData("", Constants.USUARIO_SESION_ID)
        cnx.savePersistData("", Constants.MAIL)
        cnx.savePersistData("", Constants.NOMBRE)
    }
}