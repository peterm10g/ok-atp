package com.lsh.atp.api.service.hold;

import com.lsh.atp.api.model.Hold.HoldRequest;
import com.lsh.atp.api.model.Hold.HoldResponse;

/**
 * Project Name: lsh-atp
 * Created by miaozhuang
 * Date: 16/8/18
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.api.service.hold.
 * desc:预占接口
 */
public interface IHoldRpcService {

    /**
     * 预占库存
     * @param holdRequest 请求参数
     * @return HoldResponse
     */
    HoldResponse preHoldInventory(HoldRequest holdRequest);

}
