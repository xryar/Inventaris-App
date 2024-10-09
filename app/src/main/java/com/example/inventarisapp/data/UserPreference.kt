package com.example.inventarisapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.inventarisapp.data.response.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by
    preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore:
    DataStore<Preferences>){

    suspend fun saveSession(user: UserModel){
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = user.username
        }
    }

    fun getSession(): Flow<UserModel>{
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[USERNAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?:"",
                preferences[PASSWORD_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun login(){
        dataStore.edit { preferences ->
            preferences[IS_LOGIN_KEY] = true
        }
    }

    suspend fun logout(){
        dataStore.edit { preferences ->
            preferences[IS_LOGIN_KEY] = false
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: UserPreference?= null

        private val USERNAME_KEY = stringPreferencesKey("username")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>):
            UserPreference{
            return INSTANCE ?: synchronized(this){
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}