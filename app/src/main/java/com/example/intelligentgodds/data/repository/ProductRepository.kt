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
     * 获取商品列表（从网络API获取并汉化）
     */
    suspend fun getProducts(): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "开始请求商品列表...")
            val response = apiService.getProducts()
            android.util.Log.d("ProductRepository", "成功加载 ${response.size} 个商品")
            // 汉化商品数据
            val localizedProducts = response.map { localizeProduct(it) }
            Result.success(localizedProducts)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "网络请求失败: ${e.message}, 使用本地数据降级")
            // 网络失败时使用本地数据降级
            try {
                val products = LocalDataSource.getLocalProducts()
                Result.success(products)
            } catch (localError: Exception) {
                Result.failure(localError)
            }
        }
    }
    
    /**
     * 随机获取商品（用于刷新功能）- 从网络获取并汉化
     */
    suspend fun getRandomProducts(limit: Int = 10): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "随机刷新商品...")
            val allProducts = apiService.getProducts()
            val randomProducts = allProducts.shuffled().take(limit).map { localizeProduct(it) }
            android.util.Log.d("ProductRepository", "成功获取 ${randomProducts.size} 个随机商品")
            Result.success(randomProducts)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "网络请求失败: ${e.message}, 使用本地数据降级")
            // 网络失败时使用本地数据降级
            try {
                val localProducts = LocalDataSource.getLocalProducts().shuffled().take(limit)
                Result.success(localProducts)
            } catch (localError: Exception) {
                Result.failure(localError)
            }
        }
    }
    
    /**
     * 根据ID获取商品详情 - 从网络获取并汉化
     */
    suspend fun getProductById(id: Int): Result<Product> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "请求商品详情 ID: $id")
            val product = apiService.getProductById(id)
            val localizedProduct = localizeProduct(product)
            Result.success(localizedProduct)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "网络请求失败: ${e.message}, 使用本地数据降级")
            // 网络失败时使用本地数据降级
            try {
                val localProduct = LocalDataSource.getLocalProducts().find { it.id == id }
                if (localProduct != null) {
                    Result.success(localProduct)
                } else {
                    Result.failure(Exception("未找到商品"))
                }
            } catch (localError: Exception) {
                Result.failure(localError)
            }
        }
    }
    
    /**
     * 获取分类列表 - 返回中文分类
     */
    suspend fun getCategories(): Result<List<Category>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "加载分类列表...")
            val categories = apiService.getCategories()
            // 将英文分类名映射为中文
            val localizedCategories = categories.mapNotNull { categoryName ->
                val chineseName = translateCategory(categoryName)
                if (chineseName.isNotEmpty()) {
                    Category(name = chineseName)
                } else {
                    null
                }
            }
            android.util.Log.d("ProductRepository", "成功获取 ${localizedCategories.size} 个中文分类")
            Result.success(localizedCategories)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "网络请求失败: ${e.message}, 使用本地数据降级")
            // 网络失败时使用本地数据降级
            try {
                val localCategories = LocalDataSource.getLocalCategories()
                Result.success(localCategories)
            } catch (localError: Exception) {
                Result.failure(localError)
            }
        }
    }
    
    /**
     * 根据分类获取商品 - 从网络获取并汉化
     */
    suspend fun getProductsByCategory(category: String): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "请求分类商品: $category")
            // 将中文分类名转换回英文用于API请求
            val englishCategory = reverseTranslateCategory(category)
            val products = if (englishCategory.isNotEmpty()) {
                apiService.getProductsByCategory(englishCategory)
            } else {
                apiService.getProducts()
            }
            val localizedProducts = products.map { localizeProduct(it) }
            Result.success(localizedProducts)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "网络请求失败: ${e.message}, 使用本地数据降级")
            // 网络失败时使用本地数据降级
            try {
                val localProducts = LocalDataSource.getLocalProducts()
                    .filter { it.category == category }
                Result.success(localProducts)
            } catch (localError: Exception) {
                Result.failure(localError)
            }
        }
    }
    
    /**
     * 汉化商品数据
     */
    private fun localizeProduct(product: Product): Product {
        return product.copy(
            title = translateProductName(product.title),
            description = translateDescription(product.description, product.category),
            category = translateCategory(product.category)
        )
    }
    
    /**
     * 翻译商品标题
     */
    private fun translateProductName(title: String): String {
        return productNameMap[title] ?: run {
            // 如果没有精确匹配，尝试部分匹配
            productNameMap.entries.find { entry ->
                title.contains(entry.key, ignoreCase = true)
            }?.value ?: title
        }
    }
    
    /**
     * 翻译商品描述
     */
    private fun translateDescription(description: String, category: String): String {
        // 如果已有中文描述则直接返回
        if (description.any { it in '\u4e00'..'\u9fff' }) {
            return description
        }
        
        // 根据分类生成通用中文描述
        return categoryDescriptionMap[category] ?: "优质商品，品质保证"
    }
    
    /**
     * 翻译分类名称
     */
    private fun translateCategory(category: String): String {
        return categoryMap[category] ?: category
    }
    
    /**
     * 将中文分类名反向转换为英文（用于API请求）
     */
    private fun reverseTranslateCategory(chineseCategory: String): String {
        return categoryMap.entries.find { it.value == chineseCategory }?.key ?: ""
    }
    
    // 分类映射表（英文 -> 中文）
    private val categoryMap = mapOf(
        "men's clothing" to "男装",
        "women's clothing" to "女装",
        "electronics" to "电子产品",
        "jewelery" to "珠宝饰品"
    )
    
    // 商品名称映射表（英文 -> 中文）
    private val productNameMap = mapOf(
        "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops" to "Fjallraven 折叠背包 可容纳15寸笔记本",
        "Mens Casual Premium Slim Fit T-Shirts" to "男士休闲高级修身T恤",
        "Mens Cotton Jacket" to "男士棉质夹克",
        "Mens Slim Fit Crew Neck T-Shirt" to "男士修身圆领T恤",
        "John Hardy Women's Legends Naga Gold & Silver Dragon Station Chain Bracelet" to "John Hardy 女士传奇系列金银龙链手镯",
        "Solid Gold Petite Micropave" to "纯金微型密钉戒指",
        "White Gold Plated Princess" to "白金公主方钻耳钉",
        "Pierced Owl Rose Gold Plated Stainless Steel Double" to "猫头鹰玫瑰金双层耳环",
        "WD 2TB Elements Portable External Hard Drive - USB 3.0" to "WD 2TB 移动硬盘 USB 3.0",
        "SanDisk SDSSDXPSD-1T00-X46 1 TB Ultra SSD" to "SanDisk 1TB 超高速固态硬盘",
        "Silicon Power 2TB 3D NAND A55 SLC Cache Performance Boost SATA III 2.5" to "Silicon Power 2TB 3D NAND 固态硬盘",
        "Crucial X8 1TB USB 3.2 Portable SSD" to "Crucial X8 1TB 便携式固态硬盘",
        "Acer SB220Q bi 21.5 inches Full HD IPS Ultra-Thin" to "Acer 21.5英寸全高清IPS显示器",
        "Samsung 49-Inch CHG90 Monitor" to "三星 49英寸曲面显示器",
        "BIYLACLEEN Women's 2025 Summer V Neck Ruffle" to "女士夏季V领荷叶边连衣裙",
        "Lock and Love Women's Removable Hooded Faux Leather Moto Jacket" to "女士可拆卸帽仿皮机车夹克",
        "Rain Jacket Women Windbreaker Striped Climbing Raincoats" to "女士条纹防风登山雨衣",
        "Lightweight Perfected Long Sleeve Fashion Hooded Tops" to "轻量长袖连帽时尚上衣",
        "MBJ Women's Solid Short Sleeve Boat Neck V" to "女士纯色短袖船领上衣",
        "Opna Women's Short Sleeve Moisture" to "Opna女士速干短袖T恤",
        "DANVOUY Womens T Shirt Casual Cotton Short" to "DANVOUY女士休闲棉质短袖T恤",
        "Tilaka Men's Regular-Fit Quick-Dry Yoga Polo Shirt" to "Tilaka男士速干瑜伽POLO衫",
        "Hanes Men's ComfortSoft Short Sleeve T-Shirt" to "Hanes男士舒适短袖T恤",
        "Hanes Men's Nano-T Short Sleeve T-Shirt" to "Hanes男士纳米科技短袖T恤",
        "Amazon Essentials Men's Regular-Fit Cotton Polo Shirt" to "亚马逊基础款男士纯棉POLO衫",
        "Champion Men's Classic Jersey T-Shirt" to "Champion男士经典运动T恤"
    )
    
    // 分类通用描述映射表
    private val categoryDescriptionMap = mapOf(
        "男装" to "优质面料，舒适透气，时尚设计，适合日常穿着",
        "女装" to "精致工艺，优雅设计，展现女性魅力",
        "电子产品" to "高性能配置，稳定可靠，提升生活品质",
        "珠宝饰品" to "精美工艺，闪耀夺目，彰显品味"
    )
}
