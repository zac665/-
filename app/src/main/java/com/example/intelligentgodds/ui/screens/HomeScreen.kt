package com.example.intelligentgodds.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
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
import com.example.intelligentgodds.data.model.Product
import com.example.intelligentgodds.viewmodel.ProductViewModel
import com.example.intelligentgodds.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ProductViewModel,
    onProductClick: (Int) -> Unit
) {
    val productsState by viewModel.productsState.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("商品推荐") },
                actions = {
                    IconButton(onClick = { viewModel.refreshRandomProducts() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "随机刷新")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (productsState) {
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
                val products = (productsState as UiState.Success).data
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "💡 点击顶部刷新按钮随机推荐商品",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            isFavorite = favoriteIds.contains(product.id),
                            onFavoriteClick = { viewModel.toggleFavorite(product.id) },
                            onClick = { onProductClick(product.id) }
                        )
                    }
                }
            }
            is UiState.Error -> {
                val error = (productsState as UiState.Error).message
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
                        Button(onClick = { viewModel.loadProducts() }) {
                            Text("重试")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.image)
                        .size(200, 200)
                        .crossfade(200)
                        .build(),
                    contentDescription = product.title,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp),
                    placeholder = painterResource(R.drawable.ic_image_placeholder),
                    error = painterResource(R.drawable.ic_image_placeholder)
                )
                
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "取消收藏" else "收藏",
                        tint = if (isFavorite) MaterialTheme.colorScheme.error else LocalContentColor.current
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¥${product.price}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "★ ${product.rating.rate} (${product.rating.count})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = product.category,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
