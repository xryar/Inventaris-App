package com.example.inventarisapp.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)
