package com.lsh.atp.service.inventory;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.atp.api.model.inventory.QueryInventoryRequest;
import com.lsh.atp.api.model.inventory.QueryInventory;
import com.lsh.atp.api.service.inventory.IInventoryQueryRPCService;
import com.lsh.atp.api.service.inventory.IInventoryQueryRestService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * Project Name: lsh-atp
 * Created by zhangqiang
 * Date: 16/7/6
 * Time: 16/7/6.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.atp.service.inventory
 * desc:查询库存Rest接口实现类
 */
@Service(protocol = "rest")
@Path("inventory")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8})
public class InventoryQueryRestService implements IInventoryQueryRestService{

    @Autowired
    private IInventoryQueryRPCService iInventoryQueryRPCService;


    @POST
    @Path("query")
    public QueryInventory queryInventory(QueryInventoryRequest request) {
        return iInventoryQueryRPCService.queryInventory(request);
    }
}
