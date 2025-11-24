package com.example.app_kotlin

import android.app.Application
import android.content.Context

class App : Application() {

    init {
        instance = this
    }

    companion object {
        private lateinit var instance: App
        val context: Context
            get() = instance.applicationContext
    }
}
