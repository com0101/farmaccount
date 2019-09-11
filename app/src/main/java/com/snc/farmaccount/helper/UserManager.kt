package com.snc.farmaccount.helper

import android.content.Context
import android.content.SharedPreferences
import com.snc.farmaccount.ApplicationContext

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
    var userName: String? = null

        get() {
            return preferences?.getString("Name", null)
        }

        set(value) {
            field = preferences?.edit()?.putString("Name", value)?.apply().toString()
        }
    var userEmail: String? = null

        get() {
            return preferences?.getString("email", null)
        }

        set(value) {
            field = preferences?.edit()?.putString("email", value)?.apply().toString()
        }
}