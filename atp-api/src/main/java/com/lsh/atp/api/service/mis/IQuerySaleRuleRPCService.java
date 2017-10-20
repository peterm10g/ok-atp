package com.lsh.atp.api.service.mis;

import com.lsh.atp.api.model.mis.QuerySaleRuleRequest;
import com.lsh.atp.api.model.mis.QuerySaleRuleResponse;

/**
 * Created by jingyuan on 16/10/18.
 */
public interface IQuerySaleRuleRPCService {
    QuerySaleRuleResponse querySaleRule(QuerySaleRuleRequest request);
}
