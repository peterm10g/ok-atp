package com.lsh.atp.api.service.inventory;

import com.lsh.atp.api.model.inventory.QueryInventoryRequest;
import com.lsh.atp.api.model.inventory.QueryInventory;

/**
 * 查询库存rest 接口
 * Created by zhangqiang on 16/7/6.
 */
public interface IInventoryQueryRestService {
    /**
     * 查询库存
     *
     * @param request 请求json
     *
     * @return 返回json
     * */
    QueryInventory queryInventory(QueryInventoryRequest request);
}
