package com.lsh.mis.service.salerule;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.atp.api.model.mis.QueryItemRuleRequest;
import com.lsh.atp.api.model.mis.QueryItemRuleResponse;
import com.lsh.atp.api.service.mis.IQueryItemRuleRPCService;
import com.lsh.atp.api.service.mis.IQueryItemRuleRestService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by jingyuan on 16/10/18.
 * 查询库存(分DC和出货规则)
 */
@Service(protocol = "rest")
@Path("mis")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public class QueryItemRuleRestService implements IQueryItemRuleRestService {

    @Autowired
    private IQueryItemRuleRPCService queryItemRuleRPCService;

    @POST
    @Path("querydetail")
    public QueryItemRuleResponse queryItemRuleResponse(QueryItemRuleRequest request) {
        return queryItemRuleRPCService.queryItemRuleResponse(request);
    }
}

