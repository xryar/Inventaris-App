package com.example.inventarisapp.data.api

import com.example.inventarisapp.data.response.AllProductsResponse
import com.example.inventarisapp.data.response.Data
import com.example.inventarisapp.data.response.LoginRequest
import com.example.inventarisapp.data.response.LoginResponse
import com.example.inventarisapp.data.response.RegisterRequest
import com.example.inventarisapp.data.response.RegisterResponse
import com.example.inventarisapp.data.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST("users/register")
    fun register(
        @Body registerRequest: RegisterRequest
    ): Call <RegisterResponse>

    @POST("users/login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call <LoginResponse>

    @GET("products")
    fun products(
        @Header("Authorization")token: String
    ): Call <AllProductsResponse>

    @Multipart
    @POST("products")
    fun sendProducts(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("productName") productName: RequestBody,
        @Part("category") category: RequestBody,
        @Part("quantity") quantity: RequestBody,
        @Part("price") price: RequestBody,
        @Part("date") date: RequestBody,
    ): Call <Data>
}