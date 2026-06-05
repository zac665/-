package com.example.intelligentgodds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intelligentgodds.data.local.LocalDataSource
import com.example.intelligentgodds.data.model.Category
import com.example.intelligentgodds.data.model.Product
import com.example.intelligentgodds.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class ProductViewModel : ViewModel() {
    
    private val repository = ProductRepository()
    
    // 商品列表状态
    private val _productsState = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val productsState: StateFlow<UiState<List<Product>>> = _productsState
    
    // 商品详情状态
    private val _productDetailState = MutableStateFlow<UiState<Product>>(UiState.Loading)
    val productDetailState: StateFlow<UiState<Product>> = _productDetailState
    
    // 分类列表状态
    private val _categoriesState = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categoriesState: StateFlow<UiState<List<Category>>> = _categoriesState
    
    // 收藏列表
    private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteIds: StateFlow<Set<Int>> = _favoriteIds
    
    // 缓存数据
    private var cachedProducts: List<Product>? = null
    private var cachedCategories: List<Category>? = null
    private val productDetailCache = mutableMapOf<Int, Product>()
    
    // 分页配置
    private val pageSize = 10 // 每页10个商品
    private var currentPage = 0
    private var allProducts: List<Product>? = null
    private var hasMoreData = true
    
    // 搜索相关
    private var searchKeyword: String = ""
    private var filteredProducts: List<Product>? = null
    
    /**
     * 加载商品列表（带缓存）- 分页优化版
     */
    fun loadProducts() {
        viewModelScope.launch {
            _productsState.value = UiState.Loading
            
            // 如果有缓存，立即显示（0延迟）
            cachedProducts?.let {
                android.util.Log.d("ProductViewModel", "⚡ 使用缓存数据: ${it.size} 个商品")
                _productsState.value = UiState.Success(it)
                return@launch
            }
            
            val result = repository.getProducts()
            when {
                result.isSuccess -> {
                    val products = result.getOrNull() ?: emptyList()
                    allProducts = products // 保存所有商品
                    currentPage = 1
                    val firstPage = products.take(pageSize) // 只取第一页
                    cachedProducts = firstPage
                    hasMoreData = products.size > pageSize
                    android.util.Log.d("ProductViewModel", "✅ 加载成功: 第1页 ${firstPage.size} 个商品（共${products.size}个）")
                    _productsState.value = UiState.Success(firstPage)
                }
                else -> {
                    _productsState.value = UiState.Error(result.exceptionOrNull()?.message ?: "未知错误")
                }
            }
        }
    }
    
    /**
     * 加载更多商品（分页）- 修复版
     */
    fun loadMoreProducts() {
        if (!hasMoreData || allProducts == null) {
            android.util.Log.d("ProductViewModel", "📄 没有更多数据: hasMoreData=$hasMoreData, allProducts=${allProducts?.size}")
            return
        }
        
        viewModelScope.launch {
            val nextPage = currentPage + 1
            val startIndex = currentPage * pageSize
            val endIndex = minOf(startIndex + pageSize, allProducts!!.size)
            
            if (startIndex >= allProducts!!.size) {
                android.util.Log.d("ProductViewModel", "📄 已加载所有商品")
                hasMoreData = false
                return@launch
            }
            
            val moreProducts = allProducts!!.subList(startIndex, endIndex)
            val currentList = cachedProducts ?: emptyList()
            val newList = currentList + moreProducts
            
            cachedProducts = newList
            currentPage = nextPage
            hasMoreData = endIndex < allProducts!!.size
            
            android.util.Log.d("ProductViewModel", "📄 加载更多: 第${nextPage}页, 起始:$startIndex, 结束:$endIndex, 加载${moreProducts.size}个, 当前总共${newList.size}个")
            _productsState.value = UiState.Success(newList)
        }
    }
    
    /**
     * 随机刷新商品 - 修复分页状态
     */
    fun refreshRandomProducts() {
        viewModelScope.launch {
            _productsState.value = UiState.Loading
            
            val result = repository.getRandomProducts(10)
            when {
                result.isSuccess -> {
                    val products = result.getOrNull() ?: emptyList()
                    // 修复：重置分页状态
                    allProducts = products
                    cachedProducts = products
                    currentPage = 1
                    hasMoreData = false // 随机刷新不分页
                    android.util.Log.d("ProductViewModel", "随机刷新成功: ${products.size} 个商品")
                    _productsState.value = UiState.Success(products)
                }
                else -> {
                    _productsState.value = UiState.Error(result.exceptionOrNull()?.message ?: "刷新失败")
                }
            }
        }
    }
    
    /**
     * 加载商品详情
     */
    fun loadProductDetail(productId: Int) {
        viewModelScope.launch {
            _productDetailState.value = UiState.Loading
            
            // 先检查缓存
            productDetailCache[productId]?.let {
                android.util.Log.d("ProductViewModel", "使用缓存的商品详情")
                _productDetailState.value = UiState.Success(it)
                return@launch
            }
            
            val result = repository.getProductById(productId)
            when {
                result.isSuccess -> {
                    val product = result.getOrNull()!!
                    productDetailCache[productId] = product
                    android.util.Log.d("ProductViewModel", "商品详情加载成功: ${product.title}")
                    _productDetailState.value = UiState.Success(product)
                }
                else -> {
                    _productDetailState.value = UiState.Error(result.exceptionOrNull()?.message ?: "加载失败")
                }
            }
        }
    }
    
    /**
     * 加载分类列表
     */
    fun loadCategories() {
        viewModelScope.launch {
            _categoriesState.value = UiState.Loading
            
            // 如果有缓存，直接返回
            cachedCategories?.let {
                android.util.Log.d("ProductViewModel", "使用缓存分类")
                _categoriesState.value = UiState.Success(it)
                return@launch
            }
            
            val result = repository.getCategories()
            when {
                result.isSuccess -> {
                    val categories = result.getOrNull() ?: emptyList()
                    cachedCategories = categories
                    _categoriesState.value = UiState.Success(categories)
                }
                else -> {
                    _categoriesState.value = UiState.Error(result.exceptionOrNull()?.message ?: "未知错误")
                }
            }
        }
    }
    
    /**
     * 预加载商品详情（提升用户体验）
     */
    fun preloadProductDetail(productId: Int) {
        viewModelScope.launch {
            // 如果已经缓存，不重复加载
            if (productDetailCache.containsKey(productId)) {
                return@launch
            }
            
            val result = repository.getProductById(productId)
            if (result.isSuccess) {
                val product = result.getOrNull()!!
                productDetailCache[productId] = product
                android.util.Log.d("ProductViewModel", "🔮 预加载成功: ${product.title}")
            }
        }
    }
    
    /**
     * 批量预加载（预加载前3个商品）
     */
    fun preloadNextProducts(productIds: List<Int>) {
        productIds.take(3).forEach { productId ->
            preloadProductDetail(productId)
        }
    }
    /**
     * 切换收藏状态
     */
    fun toggleFavorite(productId: Int) {
        val currentFavorites = _favoriteIds.value.toMutableSet()
        if (currentFavorites.contains(productId)) {
            currentFavorites.remove(productId)
        } else {
            currentFavorites.add(productId)
        }
        _favoriteIds.value = currentFavorites
        android.util.Log.d("ProductViewModel", "❤️ 收藏状态变更: $productId, 当前收藏数: ${currentFavorites.size}")
    }
    
    /**
     * 获取收藏的商品列表
     */
    fun getFavoriteProducts(): List<Product> {
        val allProducts = cachedProducts ?: LocalDataSource.getLocalProducts()
        return allProducts.filter { _favoriteIds.value.contains(it.id) }
    }
    
    /**
     * 搜索商品（支持标题、描述、分类）
     */
    fun searchProducts(keyword: String) {
        viewModelScope.launch {
            searchKeyword = keyword.trim()
            
            if (searchKeyword.isEmpty()) {
                // 如果搜索词为空，显示所有商品
                loadProducts()
                return@launch
            }
            
            _productsState.value = UiState.Loading
            
            val allProducts = allProducts ?: cachedProducts ?: LocalDataSource.getLocalProducts()
            
            // 在标题、描述、分类中搜索
            filteredProducts = allProducts.filter { product ->
                product.title.contains(searchKeyword, ignoreCase = true) ||
                product.description.contains(searchKeyword, ignoreCase = true) ||
                product.category.contains(searchKeyword, ignoreCase = true)
            }
            
            android.util.Log.d("ProductViewModel", "🔍 搜索 '$searchKeyword': 找到 ${filteredProducts!!.size} 个结果")
            _productsState.value = UiState.Success(filteredProducts!!)
        }
    }
    
    /**
     * 清除搜索，显示全部商品
     */
    fun clearSearch() {
        searchKeyword = ""
        filteredProducts = null
        loadProducts()
    }
    
    /**
     * 根据分类筛选商品
     */
    fun filterProductsByCategory(category: String) {
        viewModelScope.launch {
            _productsState.value = UiState.Loading
            
            val result = repository.getProductsByCategory(category)
            when {
                result.isSuccess -> {
                    val products = result.getOrNull() ?: emptyList()
                    cachedProducts = products
                    _productsState.value = UiState.Success(products)
                }
                else -> {
                    _productsState.value = UiState.Error(result.exceptionOrNull()?.message ?: "筛选失败")
                }
            }
        }
    }
}
