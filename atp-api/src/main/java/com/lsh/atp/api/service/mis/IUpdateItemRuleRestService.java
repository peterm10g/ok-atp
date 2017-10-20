package com.lsh.atp.api.service.mis;

import com.lsh.atp.api.model.baseVo.BaseResponse;
import com.lsh.atp.api.model.mis.UpdateItemRuleRequest;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang
 * Date: 16/10/19
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.api.service.mis
 * desc:更新商品售卖规则Rest接口
 */
public interface IUpdateItemRuleRestService {

    BaseResponse updateItemSaleRule(UpdateItemRuleRequest request);
}
