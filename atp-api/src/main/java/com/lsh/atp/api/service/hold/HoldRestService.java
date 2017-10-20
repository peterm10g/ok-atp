package com.lsh.atp.api.service.hold;

import com.lsh.atp.api.model.Hold.HoldRequest;
import com.lsh.atp.api.model.Hold.HoldResponse;
import com.lsh.atp.api.model.baseVo.BaseResponse;

/**
 * Project Name: lsh-atp
 * Created by miaozhuang
 * Date: 16/7/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.api.service.hold.
 * desc:预占接口
 */
public interface HoldRestService {

    /**
     *  还原库存
     */
    BaseResponse restoreInventoryByHold();

    /**
     * 预占库存
     *
     * @param holdRequest 请求参数
     * @return HoldResponse
     */
    HoldResponse preHold(HoldRequest holdRequest);
}
