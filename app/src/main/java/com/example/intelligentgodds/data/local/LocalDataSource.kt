package com.example.intelligentgodds.data.local

import com.example.intelligentgodds.data.model.Category
import com.example.intelligentgodds.data.model.Product
import com.example.intelligentgodds.data.model.Rating

object LocalDataSource {
    
    /**
     * 获取本地商品列表（用于网络失败时降级）
     */
    fun getLocalProducts(): List<Product> {
        return listOf(
            Product(
                id = 1,
                title = "男士休闲高级修身T恤",
                price = 22.3,
                description = "优质棉质面料，舒适透气，适合日常穿着。修身设计展现完美身材线条。",
                category = "男装",
                image = "https://loremflickr.com/400/400/product,1/all",
                rating = Rating(rate = 4.1, count = 259)
            ),
            Product(
                id = 2,
                title = "男士修身运动长裤",
                price = 15.2,
                description = "轻便透气的面料，修身剪裁，弹力腰带设计，适合运动和休闲场合。",
                category = "男装",
                image = "https://loremflickr.com/400/400/product,2/all",
                rating = Rating(rate = 4.1, count = 235)
            ),
            Product(
                id = 3,
                title = "女士纯棉印花T恤",
                price = 29.9,
                description = "采用优质纯棉面料，柔软亲肤，时尚印花设计，展现女性优雅气质。",
                category = "女装",
                image = "https://loremflickr.com/400/400/product,3/all",
                rating = Rating(rate = 4.7, count = 500)
            ),
            Product(
                id = 4,
                title = "女士优雅雪纺连衣裙",
                price = 45.0,
                description = "轻盈飘逸的雪纺面料，优雅的A字版型，适合各种场合穿着。",
                category = "女装",
                image = "https://loremflickr.com/400/400/product,4/all",
                rating = Rating(rate = 4.5, count = 380)
            ),
            Product(
                id = 5,
                title = "WD 2TB 移动硬盘 USB 3.0",
                price = 64.99,
                description = "高速USB 3.0接口，2TB大容量存储，兼容Windows和Mac系统。",
                category = "电子产品",
                image = "https://loremflickr.com/400/400/product,5/all",
                rating = Rating(rate = 4.8, count = 1200)
            ),
            Product(
                id = 6,
                title = "SanDisk 128GB 高速U盘",
                price = 18.9,
                description = "USB 3.0高速传输，128GB大容量，小巧便携，即插即用。",
                category = "电子产品",
                image = "https://loremflickr.com/400/400/product,6/all",
                rating = Rating(rate = 4.6, count = 890)
            ),
            Product(
                id = 7,
                title = "白金订婚戒指 925银",
                price = 9.5,
                description = "925纯银材质，镶嵌高品质锆石，精致工艺，象征永恒爱情。",
                category = "珠宝饰品",
                image = "https://loremflickr.com/400/400/product,7/all",
                rating = Rating(rate = 4.3, count = 150)
            ),
            Product(
                id = 8,
                title = "施华洛世奇水晶项链",
                price = 120.0,
                description = "奥地利进口水晶，精美切割工艺，闪耀夺目，送礼佳品。",
                category = "珠宝饰品",
                image = "https://loremflickr.com/400/400/product,8/all",
                rating = Rating(rate = 4.9, count = 320)
            ),
            Product(
                id = 9,
                title = "索尼无线蓝牙耳机 WH-1000XM4",
                price = 248.0,
                description = "业界领先的降噪技术，30小时续航，触控操作，高音质体验。",
                category = "电子产品",
                image = "https://loremflickr.com/400/400/product,9/all",
                rating = Rating(rate = 4.9, count = 2500)
            ),
            Product(
                id = 10,
                title = "Apple AirPods Pro 2代",
                price = 189.0,
                description = "主动降噪，空间音频，自适应通透模式，MagSafe充电盒。",
                category = "电子产品",
                image = "https://loremflickr.com/400/400/product,10/all",
                rating = Rating(rate = 4.8, count = 3200)
            ),
            Product(
                id = 11,
                title = "Nike Air Max 运动鞋",
                price = 89.9,
                description = "经典气垫设计，舒适缓震，透气网面，时尚百搭。",
                category = "鞋靴",
                image = "https://loremflickr.com/400/400/product,11/all",
                rating = Rating(rate = 4.5, count = 1800)
            ),
            Product(
                id = 12,
                title = "Adidas Ultraboost 跑步鞋",
                price = 120.0,
                description = "Boost中底科技，Primeknit鞋面，能量回馈，极致舒适。",
                category = "鞋靴",
                image = "https://loremflickr.com/400/400/product,12/all",
                rating = Rating(rate = 4.7, count = 2100)
            ),
            Product(
                id = 13,
                title = "华为MateBook D15 笔记本电脑",
                price = 699.0,
                description = "15.6英寸全面屏，AMD Ryzen处理器，16GB内存，512GB SSD。",
                category = "电子产品",
                image = "https://loremflickr.com/400/400/product,13/all",
                rating = Rating(rate = 4.6, count = 950)
            ),
            Product(
                id = 14,
                title = "小米智能手环8 Pro",
                price = 49.9,
                description = "1.74英寸AMOLED屏幕，GPS定位，心率监测，14天续航。",
                category = "电子产品",
                image = "https://loremflickr.com/400/400/product,14/all",
                rating = Rating(rate = 4.4, count = 1500)
            ),
            Product(
                id = 15,
                title = "兰蔻小黑瓶精华肌底液 50ml",
                price = 89.0,
                description = "法国原装进口，修护肌肤屏障，提亮肤色，抗衰老。",
                category = "美妆护肤",
                image = "https://loremflickr.com/400/400/product,15/all",
                rating = Rating(rate = 4.8, count = 2800)
            ),
            Product(
                id = 16,
                title = "SK-II神仙水护肤精华 230ml",
                price = 155.0,
                description = "日本进口，PITERA精华，改善肌肤质地，提升透明度。",
                category = "美妆护肤",
                image = "https://loremflickr.com/400/400/product,16/all",
                rating = Rating(rate = 4.9, count = 3500)
            ),
            Product(
                id = 17,
                title = "三只松鼠坚果大礼包",
                price = 28.8,
                description = "精选8种坚果零食，独立包装，营养丰富，送礼自用两相宜。",
                category = "食品生鲜",
                image = "https://loremflickr.com/400/400/product,17/all",
                rating = Rating(rate = 4.6, count = 5000)
            ),
            Product(
                id = 18,
                title = "星巴克咖啡豆 250g",
                price = 35.0,
                description = "阿拉比卡咖啡豆，中度烘焙，香气浓郁，口感顺滑。",
                category = "食品生鲜",
                image = "https://loremflickr.com/400/400/product,18/all",
                rating = Rating(rate = 4.7, count = 1800)
            ),
            Product(
                id = 19,
                title = "乐高机械组兰博基尼跑车",
                price = 49.9,
                description = "3696块积木，1:8比例还原，可开启车门，收藏级模型。",
                category = "玩具游戏",
                image = "https://loremflickr.com/400/400/product,19/all",
                rating = Rating(rate = 4.9, count = 980)
            ),
            Product(
                id = 20,
                title = "飞利浦电动牙刷 HX6730",
                price = 59.0,
                description = "声波震动技术，3种清洁模式，智能计时器，2周续航。",
                category = "个护健康",
                image = "https://loremflickr.com/400/400/product,20/all",
                rating = Rating(rate = 4.5, count = 2200)
            )
        )
    }
    
    /**
     * 获取分类列表
     */
    fun getLocalCategories(): List<Category> {
        return listOf(
            Category(name = "男装"),
            Category(name = "女装"),
            Category(name = "电子产品"),
            Category(name = "珠宝饰品"),
            Category(name = "鞋靴"),
            Category(name = "美妆护肤"),
            Category(name = "食品生鲜"),
            Category(name = "玩具游戏"),
            Category(name = "个护健康")
        )
    }
}
