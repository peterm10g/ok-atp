package com.lsh.mis.service.salerule;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.atp.api.model.baseVo.ExceptionStatus;
import com.lsh.atp.api.model.baseVo.ItemDetail;
import com.lsh.atp.api.model.mis.QueryItemRuleRequest;
import com.lsh.atp.api.model.mis.QueryItemRuleResponse;
import com.lsh.atp.api.service.mis.IQueryItemRuleRPCService;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.service.mis.SaleRuleService;
import com.lsh.atp.core.util.AtpAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by jingyuan on 16/10/18.
 * 查询库存(分DC和出货规则)
 */
@Service(protocol = "dubbo")
public class QueryItemRuleRPCService implements IQueryItemRuleRPCService {

    private static Logger logger = LoggerFactory.getLogger(QueryItemRuleRPCService.class);

    @Autowired
    private SaleRuleService querySaleRuleService;

    public QueryItemRuleResponse queryItemRuleResponse(QueryItemRuleRequest request) {
        QueryItemRuleResponse response = new QueryItemRuleResponse();
        response.setDataKey(new Date());
        response.setZoneCode(request.getZoneCode());
        try {

            this.validateParams(request);

            List<ItemDetail> itemDetails;
            //Redis查询
            itemDetails = querySaleRuleService.queryItemWeightRedis(request);
            response.setItems(itemDetails);
            response.setStatus(0);
            response.setMeg("OK");
        } catch (BusinessException e) {
            response.setStatus(Integer.parseInt(e.getCode()));
            response.setMeg(e.getMessage());
            logger.error("查询库存异常," + e.getCode() + e.getMessage());
        } catch (Exception e) {
            response.setStatus(3001001);
            response.setMeg("服务端异常");
            logger.error("查询库存,服务端异常:", e);
        }
        return response;
    }

    private void validateParams(QueryItemRuleRequest request) {
        AtpAssert.notNull(request.getZoneCode(), ExceptionStatus.E1002001.getCode(),"zoneCode不能为空");
        AtpAssert.notEmpty(request.getItems(),ExceptionStatus.E1002001.getCode(),"items不能为空");
        for(Long item : request.getItems()){
            AtpAssert.notNull(item,ExceptionStatus.E1002001.getCode(),"item不能为null");
        }
    }
}
