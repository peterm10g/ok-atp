package com.lsh.atp.core.constant;


public class RedisKeyConstant {

    /**
     * 升级-appkey与产品包id的对应关系
     */
    public static final String UP_PKG_KEY = "up:pkg:key:{0}";
    /**
     * 升级-产品包版本信息
     */
    public static final String UP_PKG_VER = "up:pkg:ver:{0}";
    /**
     * 升级-版本详细信息
     */
    public static final String UP_VER_INFO = "up:ver:info:{0}";
    /**
     * 升级-升级规则
     */
    public static final String UP_RULE = "up:rule:{0}";
    /**
     * 升级-版本规则列表
     */
    public static final String UP_VER_RULE_LIST = "up:ver:rule:list:{0}";
    /**
     * 升级-版本规则关联信息
     */
    public static final String UP_VER_RULE_INFO = "up:ver:rule:info:{0}";
    /**
     * 升级-版本规则条件列表
     */
    public static final String UP_VER_RULE_CONLIST = "up:ver:rule:conlist:{0}";
    /**
     * 升级-版本规则条件明细
     */
    public static final String UP_VER_RULE_CONINFO = "up:ver:rule:coninfo:{0}";

    /********媒资相关********/
    public static final String MD_VIDEO_INFO = "md:video:info:{0}";
    public static final String MD_IMAGE_INFO = "md:image:info:{0}";


    /*------------------------------   ATP KEY ------------------------------------------------ */

    /**kafka同步库存的messageId**/
    public static final String KAFKA_MESSAGE_HANDLED = "atp:kafka:messageId:handled:{0}";

    /**kafka同步库存失败信息**/
    public static final String KAFKA_MESSAGE_FAILURE = "atp:kafka:messageId:failure:{0}";

    /**
     * kafka库存最后修改时间 hash key:{sypply:dc} value:{qty}
     * {0} zoneCode
     * {1} item
     */
    public static final String KAFKA_UPDATED = "atp:kafka:updated:{0}:{1}";

    /** area对应的区域码 **/
    public static final String ZONE_AREA = "area:zone:{0}";

    /** 预留buffer配置 **/
    public static final String SKU_BUFFER_CONF = "atp:buffer:confs";

    /**
     * 扣减库存redis锁
     */
    public static final String HOLD_INVENTORY_ITEM = "hold:inventory:{0}";

    /**
     * 该商品当前售卖仓库 hash subZone-currentDC
     *
     * {0} zoneCode
     * {1} itemId
     */
    public static final String CURRENT_SALE_DC = "atp:currentSaleDc:{0}:{1}";

    /**
     * 逻辑库存缓存 hash key:{sypply:dc} value:{qty}
     * {0} zoneCode
     * {1} item
     */
    public static final String INVENTORY_LOGIC = "atp:inventoryLogic:{0}:{1}";

    /**
     * 商品-货主权重 zset  value:{supply:dc} score:{weight}
     * {0} zoneCode
     * {1} subZoneCode
     * {2} itemId
     */
    public static final String ITEM_WEIGHT_ITEM = "atp:weight:item:{0}:{1}:{2}";

    /**
     * 货主:dc 对应 dcReal
     * {0} supply
     * {1} dc
     */
    public static final String REAL_DC = "atp:realDc:{0}:{1}";

    /**
     * warehouse hash supply:dc - warehouse
     * {0} zoneCode
     */
    public static final String WAREHOUSE_ZONE = "atp:warehouse:{0}";

    /**
     * 出货规则 hash supplysStr(1\2\2,1) - saleRule json
     * {0} zoneCode
     */
    public static final String SALE_RULE = "atp:saleRule:{0}";


}
