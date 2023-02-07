package com.example.qsdmovies.data

import android.content.Context
import android.content.SharedPreferences

class SettingsSaveData(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setDarkMode(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("Dark", state)
        editor.apply()
    }

    fun loadDarkMode(): Boolean? {
        val state = sharedPreferences.getBoolean("Dark", false)
        return (state)
    }
}