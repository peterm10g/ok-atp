package com.lsh.atp.core.constant;

public class BusiConstant {

    /**
     * 是否有效-是
     */
    public static final int EFFECTIVE_YES = 1;
    /**
     * 是否有效-否
     */
    public static final int EFFECTIVE_NO = 0;

    /**
     * 词典类型-列表
     */
    public static final int DICT_TYPE_LIST = 1;
    /**
     * 词典类型-树型
     */
    public static final int DICT_TYPE_TREE = 2;

    /**
     * 配置类型-单内容
     */
    public static final int CONFIG_PAGE_TYPE_SINGLE = 1;
    /**
     * 配置类型-多内容
     */
    public static final int CONFIG_PAGE_TYPE_MULT = 2;


    //等待审核，未审核
    public final static int AUDIT_WAIT = 0;
    //审核成功
    public final static int AUDIT_SUCCESS = 1;
    //审核失败
    public final static int AUDIT_FAIL = 2;

//    //预占
//    public final static int PREHOLD = 1;
//    //扣减
//    public final static int PREHOLD_DEDUCTION = 2;
//    //还原
//    public final static int PREHOLD_RESTORE = 3;
//    //部分还原
//    public final static int PREHOLD_SUB_RESTORE = 4;

    //库存同步来源名称
    public final static String SYSTEM_KAFKA = "kafka";
    //库存同步来源名称
    public final static String SYSTEM_LSH = "lsh";
    //库存同步来源名称
    public final static String SYSTEM_WUMART = "wumart";

    //起始页
    public final static int PAGE_START = 0;

    //页码
    public final static int PAGE_SIZE = 10000;

    //订单扣减成功
    public final static int HOLD_SUCCESS = 0;

    //订单赠品库存不足
    public final static int SUB_HOLD_SUCCESS = 1;

    //物料扣减成功
    public final static int ITEM_HOLD_SUCCESS = 1;

    //物料部分扣减成功
    public final static int ITEM_SUB_HOLD_SUCCESS = 2;

    /** 售卖规则, 仅非寄售仓库 **/
    public final static long RULE_NONCONSIGNMENT = 1L;

    /** 售卖规则, 仅寄售仓库 **/
    public final static long RULE_CONSIGNMENT = 2L;

    /** 售卖规则, 先寄售,后非寄售 **/
    public final static long RULE_ALL = 3L;

    /** 售卖规则,冻品 **/
    public final static long RULE_FROZEN = 4L;


    public final static long RULE_DC40_1 = 5L;

    public final static long RULE_DC40_2 = 6L;

    public final static long RULE_DC40_ALL = 7L;

    public final static int TYPE_COMMON = 1;
    public final static int TYPE_FROZEN = 2;

    public static final int UPDATE_STATUS = 1;
    public static final int INSERT_STATUS = 2;

}
