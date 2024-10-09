package com.example.inventarisapp.data.di

import android.content.Context
import com.example.inventarisapp.data.UserPreference
import com.example.inventarisapp.data.UserRepository
import com.example.inventarisapp.data.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository{
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}