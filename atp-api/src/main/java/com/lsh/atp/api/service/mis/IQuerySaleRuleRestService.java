package com.lsh.atp.api.service.mis;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.atp.api.model.mis.QuerySaleRuleRequest;
import com.lsh.atp.api.model.mis.QuerySaleRuleResponse;

/**
 * Created by jingyuan on 16/10/18.
 */
@Service(protocol = "dubbo")
public interface IQuerySaleRuleRestService {

    QuerySaleRuleResponse querySaleRule(QuerySaleRuleRequest request);
}
