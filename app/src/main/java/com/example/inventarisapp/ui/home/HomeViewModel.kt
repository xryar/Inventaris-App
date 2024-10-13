package com.example.inventarisapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.inventarisapp.data.UserRepository
import com.example.inventarisapp.data.api.ApiConfig
import com.example.inventarisapp.data.response.AllProductsResponse
import com.example.inventarisapp.data.response.ProductsItem
import com.example.inventarisapp.data.response.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel (private val repository: UserRepository) : ViewModel() {

    private val allProducts = MutableLiveData<ArrayList<ProductsItem>>()
    val getAllProducts : LiveData<ArrayList<ProductsItem>> = allProducts

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getProducts(token: String){
        Log.d(TAG, "Token being sent: $token")
        val client = ApiConfig.getApiService().products(token)
        client.enqueue(object: Callback<AllProductsResponse>{
            override fun onResponse(call: Call<AllProductsResponse>, response: Response<AllProductsResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    allProducts.value = ArrayList(responseBody.products)
                }else{
                    Log.d(TAG, "Error : ${response.message()}")
                    Log.d(TAG, "Error Body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<AllProductsResponse>, t: Throwable) {
                Log.d(TAG, "onFailure : ${t.message.toString()}")
            }

        })
    }

    companion object{
        const val TAG = "HomeViewModel"
    }
}