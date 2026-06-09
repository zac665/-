package com.example.intelligentgodds.data.repository

import com.example.intelligentgodds.data.model.Category
import com.example.intelligentgodds.data.model.Product
import com.example.intelligentgodds.data.network.BaiduTranslateService
import com.example.intelligentgodds.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository {
    
    private val apiService = RetrofitClient.apiService
    private val translateService = BaiduTranslateService()
    
    /**
     * 获取商品列表（从DummyJSON API获取并汉化）- 优化版
     * 先返回未翻译数据快速显示,后台异步翻译
     */
    suspend fun getProducts(): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "🌐 开始请求商品列表...")
            val response = apiService.getProducts(limit = 100, skip = 0)
            val products = response.products
            android.util.Log.d("ProductRepository", "✅ 成功加载 ${products.size} 个商品")
            
            // 优化: 先返回原始数据,让UI快速显示
            // 后台异步翻译会在ViewModel层处理
            Result.success(products)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "❌ 网络请求失败: ${e.message}", e)
            Result.failure(Exception("网络连接失败，请检查网络设置\n错误信息: ${e.message}"))
        }
    }
    
    /**
     * 随机获取商品（用于刷新功能）- 优化版
     */
    suspend fun getRandomProducts(limit: Int = 10): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "🌐 随机刷新商品...")
            val response = apiService.getProducts(limit = 100, skip = 0)
            val allProducts = response.products
            // 优化: 先返回未翻译数据
            val randomProducts = allProducts.shuffled().take(limit)
            android.util.Log.d("ProductRepository", "✅ 成功获取 ${randomProducts.size} 个随机商品")
            Result.success(randomProducts)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "❌ 网络请求失败: ${e.message}", e)
            Result.failure(Exception("网络连接失败，无法刷新商品"))
        }
    }
    
    /**
     * 根据ID获取商品详情 - 从网络获取并汉化
     */
    suspend fun getProductById(id: Int): Result<Product> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "🌐 请求商品详情 ID: $id")
            val product = apiService.getProductById(id)
            val localizedProduct = localizeProduct(product)
            android.util.Log.d("ProductRepository", "✅ 商品详情加载成功")
            Result.success(localizedProduct)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "❌ 网络请求失败: ${e.message}", e)
            Result.failure(Exception("无法加载商品详情，请检查网络连接"))
        }
    }
    
    /**
     * 获取分类列表 - 从网络获取并翻译为中文
     */
    suspend fun getCategories(): Result<List<Category>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "🌐 加载分类列表...")
            val categories = apiService.getCategories()
            // 使用翻译API将分类名翻译为中文
            val localizedCategories = categories.map { categoryName ->
                val chineseName = translateService.translate(categoryName)
                Category(name = chineseName)
            }
            android.util.Log.d("ProductRepository", "✅ 成功获取 ${localizedCategories.size} 个中文分类")
            Result.success(localizedCategories)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "❌ 网络请求失败: ${e.message}", e)
            Result.failure(Exception("无法加载分类列表，请检查网络连接"))
        }
    }
    
    /**
     * 根据分类获取商品 - 从DummyJSON获取并翻译
     */
    suspend fun getProductsByCategory(category: String): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "🌐 请求分类商品: $category")
            // 将中文分类名转换回英文用于API请求(需要反向翻译)
            val englishCategory = reverseTranslateCategoryIfNeeded(category)
            val response = if (englishCategory.isNotEmpty()) {
                apiService.getProductsByCategory(englishCategory, limit = 100)
            } else {
                apiService.getProducts(limit = 100, skip = 0)
            }
            val products = response.products
            val localizedProducts = products.map { localizeProduct(it) }
            android.util.Log.d("ProductRepository", "✅ 成功获取分类商品")
            Result.success(localizedProducts)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "❌ 网络请求失败: ${e.message}", e)
            Result.failure(Exception("无法加载分类商品，请检查网络连接"))
        }
    }
    
    /**
     * 汉化商品数据 - 使用百度翻译API自动翻译
     * 公开方法,供ViewModel后台异步调用
     */
    suspend fun localizeProduct(product: Product): Product {
        android.util.Log.d("ProductRepository", "🔄 开始翻译商品ID: ${product.id}")
        
        // 并行翻译标题、描述和分类
        val localizedTitle = translateService.translate(product.title)
        val localizedDescription = translateService.translate(product.description)
        val localizedCategory = translateService.translate(product.category)
        
        val localized = product.copy(
            title = localizedTitle,
            description = localizedDescription,
            category = localizedCategory
        )
        
        android.util.Log.d("ProductRepository", "✅ 商品ID: ${product.id} 翻译完成")
        return localized
    }
    
    /**
     * 尝试将中文分类名反向转换为英文（用于API请求）
     * 注意：由于使用动态翻译，这里只能通过缓存或常见分类映射来反查
     */
    private suspend fun reverseTranslateCategoryIfNeeded(chineseCategory: String): String {
        // 如果已经是英文，直接返回
        if (!chineseCategory.any { it in '\u4e00'..'\u9fff' }) {
            return chineseCategory
        }
        
        // 常见分类的硬编码映射（用于API请求）
        val commonCategoryMap = mapOf(
            "美妆护肤" to "beauty",
            "香水香氛" to "fragrances",
            "家具家居" to "furniture",
            "食品杂货" to "groceries",
            "家居装饰" to "home-decoration",
            "厨房用品" to "kitchen-accessories",
            "笔记本电脑" to "laptops",
            "男士衬衫" to "mens-shirts",
            "男士鞋履" to "mens-shoes",
            "男士手表" to "mens-watches",
            "手机配件" to "mobile-accessories",
            "摩托车" to "motorcycle",
            "护肤保养" to "skin-care",
            "智能手机" to "smartphones",
            "运动配件" to "sports-accessories",
            "太阳镜" to "sunglasses",
            "平板电脑" to "tablets",
            "上衣" to "tops",
            "车辆" to "vehicle",
            "女士包包" to "womens-bags",
            "女士连衣裙" to "womens-dresses",
            "女士珠宝" to "womens-jewellery",
            "女士鞋履" to "womens-shoes",
            "女士手表" to "womens-watches"
        )
        
        return commonCategoryMap[chineseCategory] ?: ""
    }
}
