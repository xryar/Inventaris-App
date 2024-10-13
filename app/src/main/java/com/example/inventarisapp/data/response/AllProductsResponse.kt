package com.example.inventarisapp.data.response

import com.google.gson.annotations.SerializedName

data class AllProductsResponse(

	@field:SerializedName("products")
	val products: List<ProductsItem>
)

data class ProductsItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("quantity")
	val quantity: Int,

	@field:SerializedName("totalPrice")
	val totalPrice: Int,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("__v")
	val V: Int,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("productName")
	val productName: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
