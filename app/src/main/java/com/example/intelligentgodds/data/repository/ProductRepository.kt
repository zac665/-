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
     * 获取商品列表（优先使用本地中文数据）
     */
    suspend fun getProducts(): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "开始请求商品列表...")
            // 直接使用本地中文数据
            val products = LocalDataSource.getLocalProducts()
            android.util.Log.d("ProductRepository", "成功加载 ${products.size} 个中文商品")
            Result.success(products)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "加载失败: ${e.message}")
            Result.failure(e)
        }
    }
    
    /**
     * 随机获取商品（用于刷新功能）- 使用本地中文数据
     */
    suspend fun getRandomProducts(limit: Int = 10): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "随机刷新商品...")
            // 从本地中文数据中随机选取
            val localProducts = LocalDataSource.getLocalProducts().shuffled().take(limit)
            android.util.Log.d("ProductRepository", "成功获取 ${localProducts.size} 个随机商品")
            Result.success(localProducts)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "加载失败: ${e.message}")
            Result.failure(e)
        }
    }
    
    /**
     * 根据ID获取商品详情 - 使用本地中文数据
     */
    suspend fun getProductById(id: Int): Result<Product> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "请求商品详情 ID: $id")
            // 直接从本地中文数据查找
            val localProduct = LocalDataSource.getLocalProducts().find { it.id == id }
            if (localProduct != null) {
                Result.success(localProduct)
            } else {
                Result.failure(Exception("未找到商品"))
            }
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "加载失败: ${e.message}")
            Result.failure(e)
        }
    }
    
    /**
     * 获取分类列表 - 使用本地中文数据
     */
    suspend fun getCategories(): Result<List<Category>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "加载分类列表...")
            val categories = LocalDataSource.getLocalCategories()
            android.util.Log.d("ProductRepository", "成功获取 ${categories.size} 个中文分类")
            Result.success(categories)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "加载失败: ${e.message}")
            Result.failure(e)
        }
    }
    
    /**
     * 根据分类获取商品 - 使用本地中文数据
     */
    suspend fun getProductsByCategory(category: String): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "请求分类商品: $category")
            // 从本地中文数据筛选
            val localProducts = LocalDataSource.getLocalProducts()
                .filter { it.category == category }
            Result.success(localProducts)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "加载失败: ${e.message}")
            Result.failure(e)
        }
    }
}
