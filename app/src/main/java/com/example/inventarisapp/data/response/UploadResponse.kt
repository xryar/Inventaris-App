package com.example.inventarisapp.data.response

import com.google.gson.annotations.SerializedName

data class UploadResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("massage")
	val massage: String,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("quantity")
	val quantity: String,

	@field:SerializedName("totalPrice")
	val totalPrice: Int,

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("productName")
	val productName: String

)
