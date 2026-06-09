package com.example.intelligentgodds.data.repository

import com.example.intelligentgodds.data.model.Category
import com.example.intelligentgodds.data.model.Product
import com.example.intelligentgodds.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository {
    
    private val apiService = RetrofitClient.apiService
    
    /**
     * 获取商品列表（从DummyJSON API获取并汉化）
     */
    suspend fun getProducts(): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "🌐 开始请求商品列表...")
            val response = apiService.getProducts(limit = 100, skip = 0)
            val products = response.products
            android.util.Log.d("ProductRepository", "✅ 成功加载 ${products.size} 个商品")
            // 汉化商品数据
            val localizedProducts = products.map { localizeProduct(it) }
            Result.success(localizedProducts)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "❌ 网络请求失败: ${e.message}", e)
            Result.failure(Exception("网络连接失败，请检查网络设置\n错误信息: ${e.message}"))
        }
    }
    
    /**
     * 随机获取商品（用于刷新功能）- 从DummyJSON获取并汉化
     */
    suspend fun getRandomProducts(limit: Int = 10): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "🌐 随机刷新商品...")
            val response = apiService.getProducts(limit = 100, skip = 0)
            val allProducts = response.products
            val randomProducts = allProducts.shuffled().take(limit).map { localizeProduct(it) }
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
     * 获取分类列表 - 返回中文分类
     */
    suspend fun getCategories(): Result<List<Category>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "🌐 加载分类列表...")
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
            android.util.Log.d("ProductRepository", "✅ 成功获取 ${localizedCategories.size} 个中文分类")
            Result.success(localizedCategories)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "❌ 网络请求失败: ${e.message}", e)
            Result.failure(Exception("无法加载分类列表，请检查网络连接"))
        }
    }
    
    /**
     * 根据分类获取商品 - 从DummyJSON获取并汉化
     */
    suspend fun getProductsByCategory(category: String): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ProductRepository", "🌐 请求分类商品: $category")
            // 将中文分类名转换回英文用于API请求
            val englishCategory = reverseTranslateCategory(category)
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
     * 汉化商品数据
     */
    private fun localizeProduct(product: Product): Product {
        android.util.Log.d("ProductRepository", "商品ID: ${product.id}, 原标题: ${product.title}, 图片URL: ${product.image}")
        val localized = product.copy(
            title = translateProductName(product.title),
            description = translateDescription(product.description, product.category),
            category = translateCategory(product.category)
        )
        android.util.Log.d("ProductRepository", "商品ID: ${product.id}, 新标题: ${localized.title}, 分类: ${localized.category}")
        return localized
    }
    
    /**
     * 翻译商品标题
     */
    private fun translateProductName(title: String): String {
        return productNameMap[title] ?: run {
            // 如果没有精确匹配，尝试部分匹配
            val matched = productNameMap.entries.find { entry ->
                title.contains(entry.key, ignoreCase = true)
            }?.value
            
            // 如果还是没有匹配，返回原标题（DummyJSON已经是清晰的英文标题）
            matched ?: title
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
        
        // 根据分类提供简短的中文说明（保留英文详细描述）
        val categoryIntro = when(category) {
            "beauty" -> "【美妆护肤】"
            "fragrances" -> "【香水香氛】"
            "furniture" -> "【家具家居】"
            "groceries" -> "【食品杂货】"
            "home-decoration" -> "【家居装饰】"
            "kitchen-accessories" -> "【厨房用品】"
            "laptops" -> "【笔记本电脑】"
            "mens-shirts" -> "【男士衬衫】"
            "mens-shoes" -> "【男士鞋履】"
            "mens-watches" -> "【男士手表】"
            "mobile-accessories" -> "【手机配件】"
            "motorcycle" -> "【摩托车】"
            "skin-care" -> "【护肤保养】"
            "smartphones" -> "【智能手机】"
            "sports-accessories" -> "【运动配件】"
            "sunglasses" -> "【太阳镜】"
            "tablets" -> "【平板电脑】"
            "tops" -> "【上衣】"
            "vehicle" -> "【车辆】"
            "womens-bags" -> "【女士包包】"
            "womens-dresses" -> "【女士连衣裙】"
            "womens-jewellery" -> "【女士珠宝】"
            "womens-shoes" -> "【女士鞋履】"
            "womens-watches" -> "【女士手表】"
            else -> ""
        }
        
        // 在描述前添加分类标签，方便用户理解
        return if (categoryIntro.isNotEmpty()) {
            "$categoryIntro $description"
        } else {
            description
        }
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
    
    // 分类映射表（英文 -> 中文）- DummyJSON 分类
    private val categoryMap = mapOf(
        "beauty" to "美妆护肤",
        "fragrances" to "香水香氛",
        "furniture" to "家具家居",
        "groceries" to "食品杂货",
        "home-decoration" to "家居装饰",
        "kitchen-accessories" to "厨房用品",
        "laptops" to "笔记本电脑",
        "mens-shirts" to "男士衬衫",
        "mens-shoes" to "男士鞋履",
        "mens-watches" to "男士手表",
        "mobile-accessories" to "手机配件",
        "motorcycle" to "摩托车",
        "skin-care" to "护肤保养",
        "smartphones" to "智能手机",
        "sports-accessories" to "运动配件",
        "sunglasses" to "太阳镜",
        "tablets" to "平板电脑",
        "tops" to "上衣",
        "vehicle" to "车辆",
        "womens-bags" to "女士包包",
        "womens-dresses" to "女士连衣裙",
        "womens-jewellery" to "女士珠宝",
        "womens-shoes" to "女士鞋履",
        "womens-watches" to "女士手表"
    )
    
    // 商品名称映射表（DummyJSON商品完整汉化）
    private val productNameMap: Map<String, String> = mapOf(
        // 美妆护肤 (Beauty)
        "Essence Mascara Lash Princess" to "艾森丝睫毛膏 - 睫毛公主",
        "Eyeshadow Palette with Mirror" to "眼影盘带镜子 - 多色眼影套装",
        "Powder Canister" to "定妆粉罐 - 轻盈控油蜜粉",
        "Red Lipstick" to "红色口红 - 经典持久唇膏",
        "Red Nail Polish" to "红色指甲油 - 快干亮泽美甲油",
        
        // 香水香氛 (Fragrances)
        "Calvin Klein CK One" to "卡尔文克莱恩 CK One 中性香水",
        "Chanel Coco Noir Eau De" to "香奈儿可可小姐黑色香水",
        "Dior J'adore" to "迪奥真我女士香水",
        "Dolce Shine Eau de" to "杜嘉班纳阳光闪耀女士香水",
        "Gucci Bloom Eau de" to "古驰花悦女士香水",
        
        // 家具家居 (Furniture)
        "Annibale Colombo Bed" to "安尼巴莱科伦博双人床 - 意式奢华",
        "Annibale Colombo Sofa" to "安尼巴莱科伦博沙发 - 现代简约",
        "Bedside Table African Cherry" to "非洲樱桃木床头柜",
        "Knoll Saarinen Executive Conference Chair" to "诺尔萨里宁行政会议椅",
        "Wooden Bathroom Sink With Mirror" to "木质浴室洗手台带镜子",
        
        // 电子产品 - 手机 (Smartphones)
        "iPhone 9" to "苹果 iPhone 9 智能手机",
        "iPhone X" to "苹果 iPhone X 全面屏手机",
        "Samsung Universe 9" to "三星 Universe 9 智能手机",
        "OPPOF19" to "OPPO F19 智能手机",
        "Huawei P30" to "华为 P30 徕卡三摄手机",
        
        // 电子产品 - 笔记本 (Laptops)
        "MacBook Pro" to "苹果 MacBook Pro 专业笔记本电脑",
        "Samsung Galaxy Book" to "三星 Galaxy Book 轻薄笔记本",
        "Microsoft Surface Laptop 4" to "微软 Surface Laptop 4 触控笔记本",
        "Infinix INBOOK" to "Infinix INBOOK 超薄笔记本",
        "HP Pavilion 15-DK1056WM" to "惠普 Pavilion 15 游戏笔记本",
        
        // 电子产品 - 平板 (Tablets)
        "iPad Mini 2021" to "苹果 iPad Mini 2021 平板电脑",
        "Samsung Galaxy Tab S8" to "三星 Galaxy Tab S8 平板",
        "Huawei MatePad 11" to "华为 MatePad 11 平板电脑",
        "Lenovo Tab P11 Plus" to "联想 Tab P11 Plus 平板",
        
        // 食品杂货 (Groceries)
        "Beef Steak" to "牛排 - 优质牛肉切片",
        "Cat Food" to "猫粮 - 营养配方宠物食品",
        "Chicken Meat" to "鸡肉 - 新鲜鸡胸肉",
        "Cooking Oil" to "食用油 - 健康植物油",
        "Cucumber" to "黄瓜 - 新鲜蔬菜",
        "Dog Food" to "狗粮 - 全营养配方",
        "Eggs" to "鸡蛋 - 新鲜农场蛋",
        "Fish Steak" to "鱼排 - 深海鱼肉",
        "Green Bell Pepper" to "青椒 - 新鲜彩椒",
        "Green Chili Pepper" to "青辣椒 - 香辣调味菜",
        "Honey Jar" to "蜂蜜罐 - 天然纯蜂蜜",
        "Ice Cream" to "冰淇淋 - 奶油口味甜品",
        "Juice" to "果汁 - 鲜榨橙汁",
        "Kiwi" to "猕猴桃 - 新鲜奇异果",
        
        // 男士衬衫 (Men's Shirts)
        "Classic Yellow Shirt" to "经典黄色衬衫 - 商务休闲",
        "Blue Shirt" to "蓝色衬衫 - 纯棉舒适",
        "White T-Shirt" to "白色T恤 - 基础百搭款",
        "Salmon T-Shirt" to "三文鱼色T恤 - 时尚休闲",
        "Oxford Shirt" to "牛津衬衫 - 经典格纹",
        
        // 男士鞋履 (Men's Shoes)
        "Nike Air Jordan" to "耐克 Air Jordan 篮球鞋",
        "Adidas Ultraboost" to "阿迪达斯 Ultraboost 跑步鞋",
        "Puma Sneakers" to "彪马运动鞋 - 轻便舒适",
        "Leather Formal Shoes" to "真皮正装皮鞋",
        "Casual Loafers" to "休闲乐福鞋",
        
        // 男士手表 (Men's Watches)
        "Rolex Submariner" to "劳力士潜航者手表",
        "Omega Speedmaster" to "欧米茄超霸系列手表",
        "Casio G-Shock" to "卡西欧 G-Shock 运动手表",
        "Seiko Automatic" to "精工自动机械表",
        "TAG Heuer Carrera" to "泰格豪雅卡莱拉手表",
        
        // 女士包包 (Women's Bags)
        "Leather Handbag" to "真皮手提包 - 优雅大容量",
        "Crossbody Bag" to "斜挎包 - 时尚单肩包",
        "Clutch Purse" to "晚宴手拿包",
        "Backpack Women" to "女士双肩背包",
        "Tote Bag" to "托特包 - 购物通勤包",
        
        // 女士连衣裙 (Women's Dresses)
        "Summer Floral Dress" to "夏季碎花连衣裙",
        "Evening Gown" to "晚礼服 - 优雅长裙",
        "Casual Maxi Dress" to "休闲长裙 - 舒适透气",
        "Party Cocktail Dress" to "派对鸡尾酒裙",
        "Bohemian Dress" to "波西米亚风格连衣裙",
        
        // 女士珠宝 (Women's Jewellery)
        "Gold Necklace" to "金项链 - 精致锁骨链",
        "Silver Bracelet" to "银手镯 - 简约时尚",
        "Pearl Earrings" to "珍珠耳环 - 优雅耳饰",
        "Diamond Ring" to "钻石戒指 - 璀璨婚戒",
        "Gemstone Pendant" to "宝石吊坠项链",
        
        // 女士鞋履 (Women's Shoes)
        "High Heels" to "高跟鞋 - 细跟优雅女鞋",
        "Running Shoes Women" to "女士跑步鞋 - 轻便透气",
        "Ballet Flats" to "芭蕾平底鞋 - 舒适日常",
        "Ankle Boots" to "短靴 - 时尚踝靴",
        "Sandals Women" to "女士凉鞋 - 夏季清爽",
        
        // 女士手表 (Women's Watches)
        "Rose Gold Watch" to "玫瑰金手表 - 优雅女表",
        "Fashion Watch Women" to "时尚女士手表",
        "Luxury Diamond Watch" to "奢华钻石女表",
        "Minimalist Watch" to "极简风格手表",
        "Smart Watch Women" to "女士智能手表",
        
        // 手机配件 (Mobile Accessories)
        "Phone Case" to "手机壳 - 防摔保护套",
        "Screen Protector" to "屏幕保护膜 - 钢化玻璃膜",
        "Wireless Charger" to "无线充电器 - 快充底座",
        "Earbuds" to "蓝牙耳机 - 真无线耳机",
        "Power Bank" to "移动电源 - 大容量充电宝",
        
        // 太阳镜 (Sunglasses)
        "Aviator Sunglasses" to "飞行员太阳镜 - 经典款式",
        "Round Sunglasses" to "圆形太阳镜 - 复古风格",
        "Sport Sunglasses" to "运动太阳镜 - 偏光镜片",
        "Cat Eye Sunglasses" to "猫眼太阳镜 - 时尚造型",
        "Oversized Sunglasses" to "大框太阳镜 - 明星同款",
        
        // 护肤保养 (Skin Care)
        "Face Moisturizer" to "面部保湿霜 - 滋润补水",
        "Vitamin C Serum" to "维生素C精华液 - 美白淡斑",
        "Sunscreen SPF50" to "防晒霜 SPF50 - 高效防护",
        "Night Cream" to "晚霜 - 夜间修护面霜",
        "Eye Cream" to "眼霜 - 淡化黑眼圈",
        
        // 家居装饰 (Home Decoration)
        "Wall Art Canvas" to "墙面艺术画 - 装饰挂画",
        "Decorative Vase" to "装饰花瓶 - 陶瓷摆件",
        "Throw Pillows" to "抱枕靠垫 - 沙发装饰",
        "LED String Lights" to "LED串灯 - 氛围装饰灯",
        "Photo Frame Set" to "相框套装 - 家庭照片墙",
        
        // 厨房用品 (Kitchen Accessories)
        "Knife Set" to "刀具套装 - 不锈钢厨刀",
        "Cutting Board" to "砧板 - 竹制切菜板",
        "Measuring Cups" to "量杯套装 - 烘焙工具",
        "Spice Rack" to "调料架 - 厨房收纳",
        "Mixing Bowls" to "搅拌碗套装 - 不锈钢盆",
        
        // 运动配件 (Sports Accessories)
        "Yoga Mat" to "瑜伽垫 - 防滑健身垫",
        "Dumbbells Set" to "哑铃套装 - 健身器材",
        "Resistance Bands" to "弹力带 - 阻力训练带",
        "Jump Rope" to "跳绳 - 有氧运动绳",
        "Water Bottle Sport" to "运动水壶 - 大容量水杯",
        
        // 摩托车 (Motorcycle)
        "Motorcycle Helmet" to "摩托车头盔 - 安全护具",
        "Riding Gloves" to "骑行手套 - 防滑耐磨",
        "Motorcycle Jacket" to "摩托车夹克 - 防风护具",
        "Exhaust System" to "排气系统 - 改装排气管",
        "LED Headlight" to "LED前大灯 - 摩托照明",
        
        // 车辆 (Vehicle)
        "Car Phone Mount" to "车载手机支架",
        "Dash Cam" to "行车记录仪 - 高清摄像头",
        "Car Vacuum Cleaner" to "车载吸尘器 - 便携清洁",
        "Seat Covers" to "汽车座套 - 通用座椅套",
        "Air Freshener" to "车载香薰 - 空气清新剂"
    )
    
    // 分类描述映射表（可选）
    private val categoryDescriptionMap = mapOf<String, String>()
    
    /**
     * 获取本地备用商品数据（当网络不可用时使用）
     */
    private fun getLocalProducts(): List<Product> {
        return listOf(
            Product(
                id = 1,
                title = "Fjallraven 折叠背包 可容纳15寸笔记本",
                price = 109.95,
                description = "优质面料，舒适透气，时尚设计，适合日常穿着",
                category = "男装",
                image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"
            ),
            Product(
                id = 2,
                title = "男士休闲高级修身T恤",
                price = 22.3,
                description = "优质面料，舒适透气，时尚设计，适合日常穿着",
                category = "男装",
                image = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg"
            ),
            Product(
                id = 3,
                title = "男士棉质夹克",
                price = 55.99,
                description = "优质面料，舒适透气，时尚设计，适合日常穿着",
                category = "男装",
                image = "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg"
            ),
            Product(
                id = 4,
                title = "男士修身圆领T恤",
                price = 15.99,
                description = "优质面料，舒适透气，时尚设计，适合日常穿着",
                category = "男装",
                image = "https://fakestoreapi.com/img/71YXzeOuslL._AC_UY879_.jpg"
            ),
            Product(
                id = 5,
                title = "John Hardy 女士传奇系列金银龙链手镯",
                price = 695.0,
                description = "精美工艺，闪耀夺目，彰显品味",
                category = "珠宝饰品",
                image = "https://fakestoreapi.com/img/71pWzhdJNwL._AC_UL640_QL65_ML3_.jpg"
            ),
            Product(
                id = 6,
                title = "纯金微型密钉戒指",
                price = 168.0,
                description = "精美工艺，闪耀夺目，彰显品味",
                category = "珠宝饰品",
                image = "https://fakestoreapi.com/img/61sbMiUnoGL._AC_UL640_QL65_ML3_.jpg"
            ),
            Product(
                id = 7,
                title = "白金公主方钻耳钉",
                price = 9.99,
                description = "精美工艺，闪耀夺目，彰显品味",
                category = "珠宝饰品",
                image = "https://fakestoreapi.com/img/51UDEzMJVpL._AC_UL640_QL65_ML3_.jpg"
            ),
            Product(
                id = 8,
                title = "猫头鹰玫瑰金双层耳环",
                price = 10.99,
                description = "精美工艺，闪耀夺目，彰显品味",
                category = "珠宝饰品",
                image = "https://fakestoreapi.com/img/71YAIFU48IL._AC_UL640_QL65_ML3_.jpg"
            ),
            Product(
                id = 9,
                title = "WD 2TB 移动硬盘 USB 3.0",
                price = 64.99,
                description = "高性能配置，稳定可靠，提升生活品质",
                category = "电子产品",
                image = "https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_.jpg"
            ),
            Product(
                id = 10,
                title = "SanDisk 1TB 超高速固态硬盘",
                price = 109.0,
                description = "高性能配置，稳定可靠，提升生活品质",
                category = "电子产品",
                image = "https://fakestoreapi.com/img/71WtwEvYDOSL._AC_UL640_QL65_ML3_.jpg"
            ),
            Product(
                id = 11,
                title = "Silicon Power 2TB 3D NAND 固态硬盘",
                price = 119.99,
                description = "高性能配置，稳定可靠，提升生活品质",
                category = "电子产品",
                image = "https://fakestoreapi.com/img/51ZymoQbLCL._AC_UL640_QL65_ML3_.jpg"
            ),
            Product(
                id = 12,
                title = "Crucial X8 1TB 便携式固态硬盘",
                price = 129.99,
                description = "高性能配置，稳定可靠，提升生活品质",
                category = "电子产品",
                image = "https://fakestoreapi.com/img/51Ug5RcWURL._AC_UL640_QL65_ML3_.jpg"
            ),
            Product(
                id = 13,
                title = "Acer 21.5英寸全高清IPS显示器",
                price = 109.99,
                description = "高性能配置，稳定可靠，提升生活品质",
                category = "电子产品",
                image = "https://fakestoreapi.com/img/81QpkIctqPL._AC_SX679_.jpg"
            ),
            Product(
                id = 14,
                title = "三星 49英寸曲面显示器",
                price = 999.99,
                description = "高性能配置，稳定可靠，提升生活品质",
                category = "电子产品",
                image = "https://fakestoreapi.com/img/81Zt42ioCgL._AC_SX679_.jpg"
            ),
            Product(
                id = 15,
                title = "女士夏季V领荷叶边连衣裙",
                price = 29.99,
                description = "精致工艺，优雅设计，展现女性魅力",
                category = "女装",
                image = "https://fakestoreapi.com/img/51Y5NIWF5jL._AC_UL640_QL65_ML3_.jpg"
            ),
            Product(
                id = 16,
                title = "女士可拆卸帽仿皮机车夹克",
                price = 59.99,
                description = "精致工艺，优雅设计，展现女性魅力",
                category = "女装",
                image = "https://fakestoreapi.com/img/71f5Eu5lJSL._AC_SX679_.jpg"
            ),
            Product(
                id = 17,
                title = "女士条纹防风登山雨衣",
                price = 39.99,
                description = "精致工艺，优雅设计，展现女性魅力",
                category = "女装",
                image = "https://fakestoreapi.com/img/71HblDSs5xL._AC_UY879_.jpg"
            ),
            Product(
                id = 18,
                title = "轻量长袖连帽时尚上衣",
                price = 25.99,
                description = "精致工艺，优雅设计，展现女性魅力",
                category = "女装",
                image = "https://fakestoreapi.com/img/71MkQ3Bui+L._AC_SX679_.jpg"
            ),
            Product(
                id = 19,
                title = "女士纯色短袖船领上衣",
                price = 19.99,
                description = "精致工艺，优雅设计，展现女性魅力",
                category = "女装",
                image = "https://fakestoreapi.com/img/51eg55uWmdL._AC_UL640_QL65_ML3_.jpg"
            ),
            Product(
                id = 20,
                title = "Opna女士速干短袖T恤",
                price = 22.99,
                description = "精致工艺，优雅设计，展现女性魅力",
                category = "女装",
                image = "https://fakestoreapi.com/img/51VG5Z6KjfL._AC_UL640_QL65_ML3_.jpg"
            )
        )
    }
    
    /**
     * 获取本地备用分类数据
     */
    private fun getLocalCategories(): List<Category> {
        return listOf(
            Category(name = "男装"),
            Category(name = "女装"),
            Category(name = "电子产品"),
            Category(name = "珠宝饰品")
        )
    }
}
