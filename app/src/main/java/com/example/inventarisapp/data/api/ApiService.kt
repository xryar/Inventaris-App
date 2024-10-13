package com.example.inventarisapp.data.api

import com.example.inventarisapp.data.response.AllProductsResponse
import com.example.inventarisapp.data.response.LoginRequest
import com.example.inventarisapp.data.response.LoginResponse
import com.example.inventarisapp.data.response.RegisterRequest
import com.example.inventarisapp.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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
        @Header("Authorization") token: String
    ): Call <AllProductsResponse>
}