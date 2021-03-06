package com.lsh.atp.api.service.decrease;

import com.lsh.atp.api.model.decrease.DecreaseInventoryRequest;
import com.lsh.atp.api.model.decrease.DecreaseInventoryResponse;

/**
 * Project Name: lsh-atp
 * Created by miaozhuang
 * Date: 16/7/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.api.service.hold.
 * desc:预占接口
 */
public interface IDecreaseInventoryRpcService {
    /**
     * 扣减库存
     * @param request
     * @return
     */
    DecreaseInventoryResponse decreaseInventory(DecreaseInventoryRequest request);
}
