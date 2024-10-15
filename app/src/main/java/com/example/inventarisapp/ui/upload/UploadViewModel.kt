package com.example.inventarisapp.ui.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inventarisapp.data.api.ApiConfig
import com.example.inventarisapp.data.response.Data
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel: ViewModel() {

    private val uploadData = MutableLiveData<Data>()
    val _uploadData: LiveData<Data> = uploadData

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

        client.enqueue(object : Callback<Data> {
            override fun onResponse(
                call: Call<Data>,
                response: Response<Data>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    uploadData.postValue(response.body())
                }else{
                    Log.d(TAG, "Error Body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                //Log.d(TAG, "Error Body: ${response.errorBody()?.string()}")
            }

        })
    }

    companion object {
        private const val TAG = "UploadViewModel"

    }
}