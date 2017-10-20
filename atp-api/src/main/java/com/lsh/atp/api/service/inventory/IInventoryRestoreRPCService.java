package com.lsh.atp.api.service.inventory;

import com.lsh.atp.api.model.Hold.HoldResponse;
import com.lsh.atp.api.model.inventory.RestoreInventoryRequest;

/**
 *
 * 还原库存Rest接口
 * Created by zhangqiang on 16/8/22.
 */
public interface IInventoryRestoreRPCService {

    HoldResponse restoreInventory(RestoreInventoryRequest request);

}
