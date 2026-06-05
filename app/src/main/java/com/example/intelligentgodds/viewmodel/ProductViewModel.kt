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
    
    /**
     * 加载商品列表（带缓存）
     */
    fun loadProducts() {
        viewModelScope.launch {
            _productsState.value = UiState.Loading
            
            // 如果有缓存，先显示缓存
            cachedProducts?.let {
                android.util.Log.d("ProductViewModel", "使用缓存数据: ${it.size} 个商品")
                _productsState.value = UiState.Success(it)
                return@launch
            }
            
            val result = repository.getProducts()
            when {
                result.isSuccess -> {
                    val products = result.getOrNull() ?: emptyList()
                    cachedProducts = products
                    android.util.Log.d("ProductViewModel", "加载成功: ${products.size} 个商品")
                    _productsState.value = UiState.Success(products)
                }
                else -> {
                    _productsState.value = UiState.Error(result.exceptionOrNull()?.message ?: "未知错误")
                }
            }
        }
    }
    
    /**
     * 随机刷新商品
     */
    fun refreshRandomProducts() {
        viewModelScope.launch {
            _productsState.value = UiState.Loading
            
            val result = repository.getRandomProducts(10)
            when {
                result.isSuccess -> {
                    val products = result.getOrNull() ?: emptyList()
                    cachedProducts = products
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
