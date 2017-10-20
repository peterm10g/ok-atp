package com.lsh.mis.service.salerule;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.atp.api.model.mis.QuerySaleRuleRequest;
import com.lsh.atp.api.model.mis.QuerySaleRuleResponse;
import com.lsh.atp.api.service.mis.IQuerySaleRuleRPCService;
import com.lsh.atp.api.service.mis.IQuerySaleRuleRestService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by jingyuan on 16/10/18.
 * 查询出货规则(根据区域)
 */
@Service(protocol = "rest")
@Path("mis")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8})
public class QuerySaleRuleRestService implements IQuerySaleRuleRestService {


    @Autowired
    private IQuerySaleRuleRPCService querySaleRuleRPCService;

    @POST
    @Path("querySaleRule")
    public QuerySaleRuleResponse querySaleRule(QuerySaleRuleRequest request) {
        return querySaleRuleRPCService.querySaleRule(request);
    }
}
