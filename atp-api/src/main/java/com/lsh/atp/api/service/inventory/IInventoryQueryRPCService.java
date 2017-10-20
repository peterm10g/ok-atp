package com.lsh.atp.api.service.inventory;

import com.lsh.atp.api.model.inventory.QueryInventory;
import com.lsh.atp.api.model.inventory.QueryInventoryRequest;

/**
 * 查询库存RPC 接口
 * Created by zhangqiang on 16/8/19.
 */
public interface IInventoryQueryRPCService {
    /**
     * 查询库存
     *
     * @param request 请求json
     *
     * @return 返回json
     * */
    QueryInventory queryInventory(QueryInventoryRequest request);
}
