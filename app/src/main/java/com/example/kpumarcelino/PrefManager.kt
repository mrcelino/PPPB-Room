package com.example.kpumarcelino

import android.content.Context
import android.content.SharedPreferences

class PrefManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_FILENAME = "AuthAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val DEFAULT_BOOLEAN = false
        private const val DEFAULT_STRING = ""

        @Volatile
        private var instance: PrefManager? = null

        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, DEFAULT_BOOLEAN)
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit().apply {
            putString(KEY_USERNAME, username)
            apply()
        }
    }

    fun savePassword(password: String) {
        sharedPreferences.edit().apply {
            putString(KEY_PASSWORD, password)
            apply()
        }
    }

    fun getUsername(): String {
        return sharedPreferences.getString(KEY_USERNAME, DEFAULT_STRING) ?: DEFAULT_STRING
    }

    fun getPassword(): String {
        return sharedPreferences.getString(KEY_PASSWORD, DEFAULT_STRING) ?: DEFAULT_STRING
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

    fun clearSpecific(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}
