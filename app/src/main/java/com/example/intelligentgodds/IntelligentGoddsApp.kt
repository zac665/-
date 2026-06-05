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
            // 内存缓存配置 - 优化：增大缓存
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.35) // 优化：增加到35%
                    .strongReferencesEnabled(true) // 强引用，防止被GC回收
                    .build()
            }
            // 磁盘缓存配置 - 优化：增大容量
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(250 * 1024 * 1024) // 优化：增加到250MB
                    .build()
            }
            // 网络配置 - 优化：更快显示
            .respectCacheHeaders(false) // 忽略缓存头，更激进缓存
            .crossfade(true)
            .crossfade(100) // 优化：减少到100ms
            .allowHardware(true) // 优化：启用硬件加速
            .build()
    }
}
