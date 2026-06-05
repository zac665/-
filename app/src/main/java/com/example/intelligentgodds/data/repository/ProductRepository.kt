package com.example.intelligentgodds.data.repository

import com.example.intelligentgodds.data.local.LocalDataSource
import com.example.intelligentgodds.data.model.Category
import com.example.intelligentgodds.data.model.Product
import com.example.intelligentgodds.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository {
    
    private val apiService = RetrofitClient.apiService
    
    /**
     * 获取商品列表（优先网络，失败时使用本地数据）
     */
    suspend fun getProducts(): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "开始请求商品列表...")
            val products = apiService.getProducts()
            android.util.Log.d("ProductRepository", "成功获取 ${products.size} 个商品")
            Result.success(products)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "网络请求失败: ${e.message}")
            android.util.Log.w("ProductRepository", "使用本地模拟数据")
            // 网络失败时使用本地数据
            Result.success(LocalDataSource.getLocalProducts())
        }
    }
    
    /**
     * 随机获取商品（用于刷新功能）
     */
    suspend fun getRandomProducts(limit: Int = 10): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "开始请求商品列表...")
            val allProducts = apiService.getProducts()
            // 随机打乱并选取指定数量
            val randomProducts = allProducts.shuffled().take(limit)
            android.util.Log.d("ProductRepository", "成功获取 ${randomProducts.size} 个随机商品")
            Result.success(randomProducts)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "网络请求失败: ${e.message}")
            android.util.Log.w("ProductRepository", "使用本地随机数据")
            // 网络失败时使用本地数据随机
            val localProducts = LocalDataSource.getLocalProducts().shuffled().take(limit)
            Result.success(localProducts)
        }
    }
    
    /**
     * 根据ID获取商品详情
     */
    suspend fun getProductById(id: Int): Result<Product> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "请求商品详情 ID: $id")
            val product = apiService.getProductById(id)
            Result.success(product)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "商品详情请求失败: ${e.message}")
            // 网络失败时使用本地数据查找
            val localProduct = LocalDataSource.getLocalProducts().find { it.id == id }
            if (localProduct != null) {
                Result.success(localProduct)
            } else {
                Result.failure(Exception("未找到商品"))
            }
        }
    }
    
    /**
     * 获取分类列表
     */
    suspend fun getCategories(): Result<List<Category>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "开始请求分类列表...")
            val categories = apiService.getCategories()
            val categoryList = categories.map { Category(name = it) }
            android.util.Log.d("ProductRepository", "成功获取 ${categoryList.size} 个分类")
            Result.success(categoryList)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "分类请求失败: ${e.message}")
            android.util.Log.w("ProductRepository", "使用本地分类数据")
            Result.success(LocalDataSource.getLocalCategories())
        }
    }
    
    /**
     * 根据分类获取商品
     */
    suspend fun getProductsByCategory(category: String): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "请求分类商品: $category")
            val products = apiService.getProductsByCategory(category)
            Result.success(products)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "分类商品请求失败: ${e.message}")
            // 网络失败时从本地数据筛选
            val localProducts = LocalDataSource.getLocalProducts()
                .filter { it.category == category }
            Result.success(localProducts)
        }
    }
}
