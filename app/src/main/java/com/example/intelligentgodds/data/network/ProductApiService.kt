package com.example.intelligentgodds.data.network

import com.example.intelligentgodds.data.model.Product
import com.example.intelligentgodds.data.model.ProductsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    // DummyJSON API - 返回包装对象 {products: [], total, skip, limit}
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 100,
        @Query("skip") skip: Int = 0
    ): ProductsResponse
    
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Int): Product
    
    @GET("products/category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category: String,
        @Query("limit") limit: Int = 100
    ): ProductsResponse
    
    @GET("products/categories")
    suspend fun getCategories(): List<String>
}
