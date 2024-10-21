package com.example.inventarisapp.ui.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inventarisapp.data.api.ApiConfig
import com.example.inventarisapp.data.response.Data
import com.example.inventarisapp.data.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel: ViewModel() {

    private val uploadData = MutableLiveData<UploadResponse>()
    val _uploadData: LiveData<UploadResponse> = uploadData

    fun uploadData(
        token: String,
        multipartBody: MultipartBody.Part,
        productName: RequestBody,
        category: RequestBody,
        quantity: RequestBody,
        price: RequestBody,
        date: RequestBody
    ) {
        val client = ApiConfig.getApiService().sendProducts(
            token, multipartBody, productName, category,
            quantity, price, date
        )

        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    uploadData.postValue(response.body())
                }else{
                    Log.d(TAG, "Error Body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    companion object {
        private const val TAG = "UploadViewModel"

    }
}