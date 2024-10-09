package com.example.inventarisapp.authentication.login

import android.content.Context
import com.example.inventarisapp.R

class TokenSession (context: Context) {

    private val loginSession = context.getSharedPreferences(context.getString(R.string.app_name), 0)

    fun saveToken(token: String){
        val editor = loginSession.edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    fun passToken(): String?{
        return loginSession.getString(TOKEN_KEY, null)
    }

    fun logoutSession(){
        loginSession.edit().clear().apply()
    }

    companion object{
        private const val TOKEN_KEY = "authToken"
    }
}