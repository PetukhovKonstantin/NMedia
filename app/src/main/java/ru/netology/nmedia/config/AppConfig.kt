package ru.netology.nmedia.config

import android.app.Application

class AppConfig: Application() {
    companion object {
        const val BASE_URL = "http://192.168.100.11:9999"
    }
}