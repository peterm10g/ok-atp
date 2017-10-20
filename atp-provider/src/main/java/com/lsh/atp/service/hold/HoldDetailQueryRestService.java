package com.lsh.atp.service.hold;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.atp.api.model.Hold.HoldDetailQueryRequest;
import com.lsh.atp.api.model.Hold.HoldDetailQueryResponse;
import com.lsh.atp.api.service.hold.IHoldDetailQueryRestService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang on 16/8/23.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
@Service(protocol = "rest")
@Path("inventory")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8})
public class HoldDetailQueryRestService implements IHoldDetailQueryRestService{

    @Autowired
    private HoldDetailQueryRPCService holdDetailQueryRPCService;

    @POST
    @Path("queryofc")
    public HoldDetailQueryResponse queryHoldDetail(HoldDetailQueryRequest request) {

        return holdDetailQueryRPCService.queryHoldDetail(request);

    }
}
