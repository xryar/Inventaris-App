package com.example.inventarisapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inventarisapp.authentication.login.LoginViewModel
import com.example.inventarisapp.authentication.register.RegisterViewModel
import com.example.inventarisapp.data.UserRepository
import com.example.inventarisapp.data.di.Injection
import com.example.inventarisapp.ui.history.HistoryViewModel
import com.example.inventarisapp.ui.home.HomeViewModel
import com.example.inventarisapp.ui.profile.ProfileViewModel
import com.example.inventarisapp.ui.upload.UploadViewModel

class ViewModelFactory(private val repository: UserRepository):
ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java) ->{
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) ->{
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->{
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->{
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel() as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) ->{
                HistoryViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown viewModel Class: " +modelClass.name)
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory{
            if (INSTANCE == null){
                synchronized(ViewModelFactory::class.java){
                    INSTANCE =
                        ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}