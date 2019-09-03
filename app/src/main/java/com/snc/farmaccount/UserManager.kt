package com.snc.farmaccount

import android.content.Context
import android.content.SharedPreferences

object UserManager {

    val preferences = ApplicationContext.applicationContext()?.
        getSharedPreferences("Token", Context.MODE_PRIVATE)

    private inline fun SharedPreferences.edit(operation:
                                                  (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var userToken: String? = null

        get() {
            return preferences?.getString("Token", null)
        }

        set(value) {
            field = preferences?.edit()?.putString("Token", value)?.apply().toString()
        }
}