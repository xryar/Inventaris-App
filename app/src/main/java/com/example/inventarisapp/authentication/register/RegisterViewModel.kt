package com.example.inventarisapp.authentication.register
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarisapp.data.UserRepository
import com.example.inventarisapp.data.api.ApiConfig
import com.example.inventarisapp.data.response.RegisterRequest
import com.example.inventarisapp.data.response.RegisterResponse
import com.example.inventarisapp.data.response.UserModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val repository: UserRepository):
ViewModel() {

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    fun setRegister(username: String, email: String, password: String, confPassword: String) {
        val registerRequest = RegisterRequest(username, email, password, confPassword)

        _registerResult.value = RegisterResult.Loading

        ApiConfig.getApiService().register(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                val responseBody = response.body()
                if (responseBody != null) {
                    _registerResult.value = RegisterResult.Success(responseBody)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody ?: "").getString("message")
                    } catch (e: Exception) {
                        "Unknown error occurred"
                    }
                    _registerResult.value = RegisterResult.Error(errorMessage)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _registerResult.value = RegisterResult.Error("Network error: ${t.message}")
            }
        })
    }

    fun saveSession(user: UserModel){
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    sealed class RegisterResult {
        object Loading : RegisterResult()
        data class Success(val data: RegisterResponse) : RegisterResult()
        data class Error(val message: String) : RegisterResult()
    }
}