package com.example.intelligentgodds.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("category")
    val category: String,
    
    // DummyJSON 使用 thumbnail 作为主图
    @SerializedName("thumbnail")
    val image: String = "",
    
    // DummyJSON 的 rating 是 Double 类型，需要转换
    @SerializedName("rating")
    private val ratingValue: Double = 0.0,
    
    @SerializedName("stock")
    val stock: Int = 0
) {
    // 兼容旧代码，提供 rating 属性
    val rating: Rating
        get() = Rating(rate = ratingValue, count = stock)
}
