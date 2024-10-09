package com.example.inventarisapp.authentication.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.inventarisapp.data.UserRepository
import com.example.inventarisapp.data.api.ApiConfig
import com.example.inventarisapp.data.response.LoginRequest
import com.example.inventarisapp.data.response.LoginResponse
import com.example.inventarisapp.data.response.UserModel
import com.example.inventarisapp.utils.Event
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: UserRepository): ViewModel() {

    private val _loginResult = MutableLiveData<Event<LoginResult>>()
    val loginResult: LiveData<Event<LoginResult>> = _loginResult

    fun setLogin(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)

        _loginResult.value = Event(LoginResult.Loading)

        ApiConfig.getApiService().login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _loginResult.value = Event(LoginResult.Success(responseBody))
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody ?: "").getString("message")
                    } catch (e: Exception) {
                        "Unknown error occurred"
                    }
                    _loginResult.value = Event(LoginResult.Error(errorMessage))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loginResult.value = Event(LoginResult.Error("Network error: ${t.message}"))
            }
        })
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(){
        viewModelScope.launch {
            repository.login()
        }
    }

    sealed class LoginResult {
        object Loading : LoginResult()
        data class Success(val data: LoginResponse) : LoginResult()
        data class Error(val message: String) : LoginResult()
    }
}