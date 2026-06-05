package com.example.intelligentgodds

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger

class IntelligentGoddsApp : Application(), ImageLoaderFactory {
    
    override fun onCreate() {
        super.onCreate()
    }
    
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            // 启用调试日志
            .logger(DebugLogger())
            // 内存缓存配置
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.30) // 30%可用内存
                    .strongReferencesEnabled(true) // 强引用，防止被GC回收
                    .build()
            }
            // 磁盘缓存配置
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(200 * 1024 * 1024) // 200MB
                    .build()
            }
            // 网络配置
            .respectCacheHeaders(false) // 忽略缓存头，更激进缓存
            .crossfade(true)
            .crossfade(200) // 200ms淡入动画
            .build()
    }
}
