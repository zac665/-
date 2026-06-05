package com.example.intelligentgodds.data.network

import com.example.intelligentgodds.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>
    
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Int): Product
    
    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<Product>
    
    @GET("products/categories")
    suspend fun getCategories(): List<String>
}
