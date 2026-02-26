package com.example.inventariosapp.domain

sealed class AppModeSelead {
    object NoSession: AppModeSelead()
    object Session: AppModeSelead()
    object Online: AppModeSelead()
    object Offline: AppModeSelead()
}