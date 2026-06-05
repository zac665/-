package com.example.intelligentgodds.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.intelligentgodds.R
import com.example.intelligentgodds.viewmodel.ProductViewModel
import com.example.intelligentgodds.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductViewModel,
    productId: Int,
    onBackClick: () -> Unit
) {
    val productState by viewModel.productDetailState.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    
    LaunchedEffect(productId) {
        viewModel.loadProductDetail(productId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("商品详情") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    if (productState is UiState.Success) {
                        val product = (productState as UiState.Success).data
                        val isFavorite = favoriteIds.contains(product.id)
                        IconButton(
                            onClick = {
                                viewModel.toggleFavorite(product.id)
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (isFavorite) "取消收藏" else "收藏",
                                tint = if (isFavorite) MaterialTheme.colorScheme.error else LocalContentColor.current
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when (productState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Success -> {
                val product = (productState as UiState.Success).data
                ProductDetailContent(
                    product = product,
                    paddingValues = paddingValues
                )
            }
            is UiState.Error -> {
                val error = (productState as UiState.Error).message
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "❌ 加载失败",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadProductDetail(productId) }) {
                            Text("重试")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailContent(
    product: com.example.intelligentgodds.data.model.Product,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 商品图片
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(product.image)
                .size(400, 400)
                .crossfade(200)
                .build(),
            contentDescription = product.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            placeholder = painterResource(R.drawable.ic_image_placeholder),
            error = painterResource(R.drawable.ic_image_placeholder)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 商品标题
        Text(
            text = product.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 价格和评分
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¥${product.price}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "★ ${product.rating.rate}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(${product.rating.count}条评价)",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Divider()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 分类
        Text(
            text = "分类",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text = product.category,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Divider()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 商品描述
        Text(
            text = "商品描述",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = androidx.compose.ui.unit.TextUnit(24f, androidx.compose.ui.unit.TextUnitType.Sp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 购买按钮
        Button(
            onClick = {
                // TODO: 实现购买功能
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "立即购买",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
