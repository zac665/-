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
                image = "https://picsum.photos/400/400?random=1",
                rating = Rating(rate = 4.1, count = 259)
            ),
            Product(
                id = 2,
                title = "男士修身运动长裤",
                price = 15.2,
                description = "轻便透气的面料，修身剪裁，弹力腰带设计，适合运动和休闲场合。",
                category = "男装",
                image = "https://picsum.photos/400/400?random=2",
                rating = Rating(rate = 4.1, count = 235)
            ),
            Product(
                id = 3,
                title = "女士纯棉印花T恤",
                price = 29.9,
                description = "采用优质纯棉面料，柔软亲肤，时尚印花设计，展现女性优雅气质。",
                category = "女装",
                image = "https://picsum.photos/400/400?random=3",
                rating = Rating(rate = 4.7, count = 500)
            ),
            Product(
                id = 4,
                title = "女士优雅雪纺连衣裙",
                price = 45.0,
                description = "轻盈飘逸的雪纺面料，优雅的A字版型，适合各种场合穿着。",
                category = "女装",
                image = "https://picsum.photos/400/400?random=4",
                rating = Rating(rate = 4.5, count = 380)
            ),
            Product(
                id = 5,
                title = "WD 2TB 移动硬盘 USB 3.0",
                price = 64.99,
                description = "高速USB 3.0接口，2TB大容量存储，兼容Windows和Mac系统。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=5",
                rating = Rating(rate = 4.8, count = 1200)
            ),
            Product(
                id = 6,
                title = "SanDisk 128GB 高速U盘",
                price = 18.9,
                description = "USB 3.0高速传输，128GB大容量，小巧便携，即插即用。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=6",
                rating = Rating(rate = 4.6, count = 890)
            ),
            Product(
                id = 7,
                title = "白金订婚戒指 925银",
                price = 9.5,
                description = "925纯银材质，镶嵌高品质锆石，精致工艺，象征永恒爱情。",
                category = "珠宝饰品",
                image = "https://picsum.photos/400/400?random=7",
                rating = Rating(rate = 4.3, count = 150)
            ),
            Product(
                id = 8,
                title = "施华洛世奇水晶项链",
                price = 120.0,
                description = "奥地利进口水晶，精美切割工艺，闪耀夺目，送礼佳品。",
                category = "珠宝饰品",
                image = "https://picsum.photos/400/400?random=8",
                rating = Rating(rate = 4.9, count = 320)
            ),
            Product(
                id = 9,
                title = "索尼无线蓝牙耳机 WH-1000XM4",
                price = 248.0,
                description = "业界领先的降噪技术，30小时续航，触控操作，高音质体验。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=9",
                rating = Rating(rate = 4.9, count = 2500)
            ),
            Product(
                id = 10,
                title = "Apple AirPods Pro 2代",
                price = 189.0,
                description = "主动降噪，空间音频，自适应通透模式，MagSafe充电盒。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=10",
                rating = Rating(rate = 4.8, count = 3200)
            ),
            Product(
                id = 11,
                title = "Nike Air Max 运动鞋",
                price = 89.9,
                description = "经典气垫设计，舒适缓震，透气网面，时尚百搭。",
                category = "鞋靴",
                image = "https://picsum.photos/400/400?random=11",
                rating = Rating(rate = 4.5, count = 1800)
            ),
            Product(
                id = 12,
                title = "Adidas Ultraboost 跑步鞋",
                price = 120.0,
                description = "Boost中底科技，Primeknit鞋面，能量回馈，极致舒适。",
                category = "鞋靴",
                image = "https://picsum.photos/400/400?random=12",
                rating = Rating(rate = 4.7, count = 2100)
            ),
            Product(
                id = 13,
                title = "华为MateBook D15 笔记本电脑",
                price = 699.0,
                description = "15.6英寸全面屏，AMD Ryzen处理器，16GB内存，512GB SSD。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=13",
                rating = Rating(rate = 4.6, count = 950)
            ),
            Product(
                id = 14,
                title = "小米智能手环8 Pro",
                price = 49.9,
                description = "1.74英寸AMOLED屏幕，GPS定位，心率监测，14天续航。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=14",
                rating = Rating(rate = 4.4, count = 1500)
            ),
            Product(
                id = 15,
                title = "兰蔻小黑瓶精华肌底液 50ml",
                price = 89.0,
                description = "法国原装进口，修护肌肤屏障，提亮肤色，抗衰老。",
                category = "美妆护肤",
                image = "https://picsum.photos/400/400?random=15",
                rating = Rating(rate = 4.8, count = 2800)
            ),
            Product(
                id = 16,
                title = "SK-II神仙水护肤精华 230ml",
                price = 155.0,
                description = "日本进口，PITERA精华，改善肌肤质地，提升透明度。",
                category = "美妆护肤",
                image = "https://picsum.photos/400/400?random=16",
                rating = Rating(rate = 4.9, count = 3500)
            ),
            Product(
                id = 17,
                title = "三只松鼠坚果大礼包",
                price = 28.8,
                description = "精选8种坚果零食，独立包装，营养丰富，送礼自用两相宜。",
                category = "食品生鲜",
                image = "https://picsum.photos/400/400?random=17",
                rating = Rating(rate = 4.6, count = 5000)
            ),
            Product(
                id = 18,
                title = "星巴克咖啡豆 250g",
                price = 35.0,
                description = "阿拉比卡咖啡豆，中度烘焙，香气浓郁，口感顺滑。",
                category = "食品生鲜",
                image = "https://picsum.photos/400/400?random=18",
                rating = Rating(rate = 4.7, count = 1800)
            ),
            Product(
                id = 19,
                title = "乐高机械组兰博基尼跑车",
                price = 49.9,
                description = "3696块积木，1:8比例还原，可开启车门，收藏级模型。",
                category = "玩具游戏",
                image = "https://picsum.photos/400/400?random=19",
                rating = Rating(rate = 4.9, count = 980)
            ),
            Product(
                id = 20,
                title = "飞利浦电动牙刷 HX6730",
                price = 59.0,
                description = "声波震动技术，3种清洁模式，智能计时器，2周续航。",
                category = "个护健康",
                image = "https://picsum.photos/400/400?random=20",
                rating = Rating(rate = 4.5, count = 2200)
            ),
            // 新增50条商品数据
            Product(
                id = 21,
                title = "小米空气净化器 Pro H",
                price = 189.0,
                description = "HEPA滤网，除甲醛PM2.5，智能感应，静音运行。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=21",
                rating = Rating(rate = 4.7, count = 1500)
            ),
            Product(
                id = 22,
                title = "戴森V12无线吸尘器",
                price = 499.0,
                description = "激光探测灰尘，强劲吸力，60分钟续航，轻量化设计。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=22",
                rating = Rating(rate = 4.9, count = 2800)
            ),
            Product(
                id = 23,
                title = "美的变频空调 1.5匹",
                price = 399.0,
                description = "一级能效，快速制冷制热，静音运行，智能控制。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=23",
                rating = Rating(rate = 4.6, count = 3200)
            ),
            Product(
                id = 24,
                title = "海尔冰箱 双开门",
                price = 599.0,
                description = "风冷无霜，节能静音，大容量存储，智能温控。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=24",
                rating = Rating(rate = 4.5, count = 1800)
            ),
            Product(
                id = 25,
                title = "九阳破壁机",
                price = 79.0,
                description = "多功能料理，加热破壁，预约定时，易清洗。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=25",
                rating = Rating(rate = 4.4, count = 2500)
            ),
            Product(
                id = 26,
                title = "优衣库轻型羽绒服",
                price = 59.0,
                description = "轻薄保暖，防风防水，便携收纳，多色可选。",
                category = "男装",
                image = "https://picsum.photos/400/400?random=26",
                rating = Rating(rate = 4.6, count = 3500)
            ),
            Product(
                id = 27,
                title = "ZARA修身西装外套",
                price = 89.0,
                description = "商务休闲两用，优质面料，修身剪裁，时尚百搭。",
                category = "男装",
                image = "https://picsum.photos/400/400?random=27",
                rating = Rating(rate = 4.3, count = 1200)
            ),
            Product(
                id = 28,
                title = "H&M纯棉衬衫",
                price = 29.0,
                description = "100%纯棉，舒适透气，经典版型，多色可选。",
                category = "男装",
                image = "https://picsum.photos/400/400?random=28",
                rating = Rating(rate = 4.2, count = 1800)
            ),
            Product(
                id = 29,
                title = "李维斯牛仔裤 501",
                price = 69.0,
                description = "经典直筒版型，优质丹宁布，耐穿耐磨，复古风格。",
                category = "男装",
                image = "https://picsum.photos/400/400?random=29",
                rating = Rating(rate = 4.7, count = 2200)
            ),
            Product(
                id = 30,
                title = "阿迪达斯运动T恤",
                price = 35.0,
                description = "速干透气，弹性面料，运动休闲，舒适贴身。",
                category = "男装",
                image = "https://picsum.photos/400/400?random=30",
                rating = Rating(rate = 4.4, count = 1600)
            ),
            Product(
                id = 31,
                title = "女士真丝连衣裙",
                price = 129.0,
                description = "100%桑蚕丝，优雅气质，修身显瘦，适合多种场合。",
                category = "女装",
                image = "https://picsum.photos/400/400?random=31",
                rating = Rating(rate = 4.8, count = 950)
            ),
            Product(
                id = 32,
                title = "韩版宽松毛衣",
                price = 49.0,
                description = "柔软舒适，宽松版型，保暖性好，秋冬必备。",
                category = "女装",
                image = "https://picsum.photos/400/400?random=32",
                rating = Rating(rate = 4.5, count = 2100)
            ),
            Product(
                id = 33,
                title = "高腰牛仔裤 修身款",
                price = 45.0,
                description = "高腰设计，修身显瘦，弹力面料，百搭时尚。",
                category = "女装",
                image = "https://picsum.photos/400/400?random=33",
                rating = Rating(rate = 4.6, count = 2800)
            ),
            Product(
                id = 34,
                title = "职业装套装 三件套",
                price = 99.0,
                description = "西装外套+马甲+长裤，专业干练，面试工作必备。",
                category = "女装",
                image = "https://picsum.photos/400/400?random=34",
                rating = Rating(rate = 4.4, count = 1500)
            ),
            Product(
                id = 35,
                title = "蕾丝雪纺上衣",
                price = 39.0,
                description = "精致蕾丝，轻盈雪纺，浪漫优雅，约会首选。",
                category = "女装",
                image = "https://picsum.photos/400/400?random=35",
                rating = Rating(rate = 4.7, count = 1800)
            ),
            Product(
                id = 36,
                title = "安踏跑步鞋 男款",
                price = 79.0,
                description = "缓震科技，透气网面，轻便舒适，专业跑步。",
                category = "鞋靴",
                image = "https://picsum.photos/400/400?random=36",
                rating = Rating(rate = 4.5, count = 3200)
            ),
            Product(
                id = 37,
                title = "新百伦运动鞋 574",
                price = 99.0,
                description = "经典复古款式，ENCAP缓震，舒适耐用。",
                category = "鞋靴",
                image = "https://picsum.photos/400/400?random=37",
                rating = Rating(rate = 4.8, count = 2500)
            ),
            Product(
                id = 38,
                title = "马丁靴 英伦风",
                price = 89.0,
                description = "真皮材质，经典8孔，耐磨防滑，百搭时尚。",
                category = "鞋靴",
                image = "https://picsum.photos/400/400?random=38",
                rating = Rating(rate = 4.6, count = 1900)
            ),
            Product(
                id = 39,
                title = "高跟鞋 细跟尖头",
                price = 69.0,
                description = "优雅细跟，尖头设计，舒适内里，职场女性首选。",
                category = "鞋靴",
                image = "https://picsum.photos/400/400?random=39",
                rating = Rating(rate = 4.3, count = 1400)
            ),
            Product(
                id = 40,
                title = "帆布鞋 低帮经典款",
                price = 39.0,
                description = "经典帆布材质，橡胶鞋底，青春活力，学生必备。",
                category = "鞋靴",
                image = "https://picsum.photos/400/400?random=40",
                rating = Rating(rate = 4.5, count = 4200)
            ),
            Product(
                id = 41,
                title = "雅诗兰黛小棕瓶精华",
                price = 129.0,
                description = "修护肌肤，抗衰老，提亮肤色，夜间修护。",
                category = "美妆护肤",
                image = "https://picsum.photos/400/400?random=41",
                rating = Rating(rate = 4.9, count = 5200)
            ),
            Product(
                id = 42,
                title = "MAC口红 子弹头",
                price = 25.0,
                description = "经典色号，持久显色，滋润不拔干，妆容点睛之笔。",
                category = "美妆护肤",
                image = "https://picsum.photos/400/400?random=42",
                rating = Rating(rate = 4.7, count = 6800)
            ),
            Product(
                id = 43,
                title = "欧莱雅面膜 补水保湿",
                price = 19.0,
                description = "深层补水，密集修护，提亮肤色，每周护理。",
                category = "美妆护肤",
                image = "https://picsum.photos/400/400?random=43",
                rating = Rating(rate = 4.5, count = 8500)
            ),
            Product(
                id = 44,
                title = "资生堂洗面奶",
                price = 35.0,
                description = "温和洁净，泡沫丰富，深层清洁，不紧绷。",
                category = "美妆护肤",
                image = "https://picsum.photos/400/400?random=44",
                rating = Rating(rate = 4.6, count = 4200)
            ),
            Product(
                id = 45,
                title = "完美日记眼影盘",
                price = 29.0,
                description = "12色组合，哑光珠光搭配，显色度高，持久不脱妆。",
                category = "美妆护肤",
                image = "https://picsum.photos/400/400?random=45",
                rating = Rating(rate = 4.4, count = 3600)
            ),
            Product(
                id = 46,
                title = "良品铺子零食大礼包",
                price = 39.0,
                description = "精选20种零食，独立包装，口味丰富，送礼佳品。",
                category = "食品生鲜",
                image = "https://picsum.photos/400/400?random=46",
                rating = Rating(rate = 4.6, count = 7200)
            ),
            Product(
                id = 47,
                title = "蒙牛纯牛奶 250ml*24盒",
                price = 45.0,
                description = "优质奶源，营养丰富，早餐必备，家庭装。",
                category = "食品生鲜",
                image = "https://picsum.photos/400/400?random=47",
                rating = Rating(rate = 4.7, count = 9500)
            ),
            Product(
                id = 48,
                title = "农夫山泉矿泉水 550ml*24瓶",
                price = 28.0,
                description = "天然水源，富含矿物质，日常饮用，健康生活。",
                category = "食品生鲜",
                image = "https://picsum.photos/400/400?random=48",
                rating = Rating(rate = 4.8, count = 12000)
            ),
            Product(
                id = 49,
                title = "金龙鱼食用油 5L",
                price = 59.0,
                description = "非转基因，物理压榨，健康烹饪，家庭装。",
                category = "食品生鲜",
                image = "https://picsum.photos/400/400?random=49",
                rating = Rating(rate = 4.5, count = 6800)
            ),
            Product(
                id = 50,
                title = "五常大米 10斤装",
                price = 49.0,
                description = "东北优质大米，颗粒饱满，香软可口，产地直供。",
                category = "食品生鲜",
                image = "https://picsum.photos/400/400?random=50",
                rating = Rating(rate = 4.9, count = 8200)
            ),
            Product(
                id = 51,
                title = "乐高城市系列警察局",
                price = 89.0,
                description = "743块积木，含4个人仔，可拼建警车和直升机。",
                category = "玩具游戏",
                image = "https://picsum.photos/400/400?random=51",
                rating = Rating(rate = 4.8, count = 1500)
            ),
            Product(
                id = 52,
                title = "芭比娃娃套装",
                price = 59.0,
                description = "精美礼盒装，多套服装配件，女孩梦想礼物。",
                category = "玩具游戏",
                image = "https://picsum.photos/400/400?random=52",
                rating = Rating(rate = 4.6, count = 2800)
            ),
            Product(
                id = 53,
                title = "遥控车 越野车模型",
                price = 79.0,
                description = "四驱驱动，高速越野，充电电池，耐摔耐用。",
                category = "玩具游戏",
                image = "https://picsum.photos/400/400?random=53",
                rating = Rating(rate = 4.5, count = 1900)
            ),
            Product(
                id = 54,
                title = "拼图 1000片 世界名画",
                price = 29.0,
                description = "高质量拼图，艺术欣赏，益智休闲，亲子互动。",
                category = "玩具游戏",
                image = "https://picsum.photos/400/400?random=54",
                rating = Rating(rate = 4.7, count = 3200)
            ),
            Product(
                id = 55,
                title = "魔方 三阶专业版",
                price = 19.0,
                description = "顺滑转动，专业竞速，益智训练，比赛专用。",
                category = "玩具游戏",
                image = "https://picsum.photos/400/400?random=55",
                rating = Rating(rate = 4.4, count = 4500)
            ),
            Product(
                id = 56,
                title = "欧姆龙血压计 上臂式",
                price = 89.0,
                description = "精准测量，语音播报，大屏显示，健康监护。",
                category = "个护健康",
                image = "https://picsum.photos/400/400?random=56",
                rating = Rating(rate = 4.8, count = 3500)
            ),
            Product(
                id = 57,
                title = "按摩仪 颈椎按摩器",
                price = 69.0,
                description = "热敷功能，多档调节，缓解疲劳，办公室必备。",
                category = "个护健康",
                image = "https://picsum.photos/400/400?random=57",
                rating = Rating(rate = 4.5, count = 2800)
            ),
            Product(
                id = 58,
                title = "足浴盆 全自动按摩",
                price = 99.0,
                description = "恒温加热，气泡按摩，折叠收纳，养生保健。",
                category = "个护健康",
                image = "https://picsum.photos/400/400?random=58",
                rating = Rating(rate = 4.6, count = 2200)
            ),
            Product(
                id = 59,
                title = "体温计 电子红外线",
                price = 39.0,
                description = "秒速测温，非接触式，精准可靠，家庭必备。",
                category = "个护健康",
                image = "https://picsum.photos/400/400?random=59",
                rating = Rating(rate = 4.7, count = 5200)
            ),
            Product(
                id = 60,
                title = "体重秤 智能体脂秤",
                price = 49.0,
                description = "蓝牙连接，APP管理，多项数据监测，健康管理。",
                category = "个护健康",
                image = "https://picsum.photos/400/400?random=60",
                rating = Rating(rate = 4.5, count = 3800)
            ),
            Product(
                id = 61,
                title = "iPad Air 平板电脑",
                price = 599.0,
                description = "10.9英寸屏幕，M1芯片，支持Apple Pencil。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=61",
                rating = Rating(rate = 4.9, count = 4500)
            ),
            Product(
                id = 62,
                title = "机械键盘 RGB背光",
                price = 79.0,
                description = "青轴手感，全键无冲，炫彩背光，游戏办公两用。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=62",
                rating = Rating(rate = 4.6, count = 3200)
            ),
            Product(
                id = 63,
                title = "罗技无线鼠标",
                price = 39.0,
                description = "人体工学设计，静音点击，长续航，办公必备。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=63",
                rating = Rating(rate = 4.7, count = 5800)
            ),
            Product(
                id = 64,
                title = "显示器 27英寸 2K",
                price = 299.0,
                description = "IPS面板，75Hz刷新率，窄边框，护眼技术。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=64",
                rating = Rating(rate = 4.8, count = 2100)
            ),
            Product(
                id = 65,
                title = "移动电源 20000mAh",
                price = 49.0,
                description = "大容量快充，双向快充，LED显示，多重保护。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=65",
                rating = Rating(rate = 4.5, count = 6500)
            ),
            Product(
                id = 66,
                title = "智能手表 运动手环",
                price = 59.0,
                description = "心率监测，睡眠分析，消息提醒，长续航。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=66",
                rating = Rating(rate = 4.4, count = 4200)
            ),
            Product(
                id = 67,
                title = "蓝牙耳机 入耳式",
                price = 39.0,
                description = "降噪通话，触控操作，IPX7防水，运动专用。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=67",
                rating = Rating(rate = 4.3, count = 5500)
            ),
            Product(
                id = 68,
                title = "路由器 WiFi6 千兆",
                price = 89.0,
                description = "双频并发，Mesh组网，信号覆盖广，稳定快速。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=68",
                rating = Rating(rate = 4.6, count = 2800)
            ),
            Product(
                id = 69,
                title = "摄像头 1080P高清",
                price = 59.0,
                description = "夜视功能，双向语音，移动侦测，云存储。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=69",
                rating = Rating(rate = 4.5, count = 3500)
            ),
            Product(
                id = 70,
                title = "打印机 激光黑白",
                price = 199.0,
                description = "高速打印，双面打印，无线连接，办公家用。",
                category = "电子产品",
                image = "https://picsum.photos/400/400?random=70",
                rating = Rating(rate = 4.7, count = 1800)
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
