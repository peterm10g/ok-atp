package com.lsh.atp.api.service.mis;

import com.lsh.atp.api.model.mis.QueryItemRuleRequest;
import com.lsh.atp.api.model.mis.QueryItemRuleResponse;

/**
 * Created by jingyuan on 16/10/18.
 * 查询库存(分DC和出货规则)
 */
public interface IQueryItemRuleRestService {
    QueryItemRuleResponse queryItemRuleResponse(QueryItemRuleRequest request);
}

