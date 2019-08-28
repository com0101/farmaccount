package com.snc.farmaccount

import android.app.Application
import android.content.Context

class ApplicationContext : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: ApplicationContext? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = ApplicationContext.applicationContext()
    }
}