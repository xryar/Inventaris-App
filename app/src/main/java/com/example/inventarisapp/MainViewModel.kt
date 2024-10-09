package com.example.inventarisapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.inventarisapp.data.UserRepository
import com.example.inventarisapp.data.response.UserModel

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }
}