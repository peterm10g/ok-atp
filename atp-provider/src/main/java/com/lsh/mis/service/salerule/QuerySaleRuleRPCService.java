package com.lsh.mis.service.salerule;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.atp.api.model.baseVo.ExceptionStatus;
import com.lsh.atp.api.model.baseVo.Salerule;
import com.lsh.atp.api.model.mis.QuerySaleRuleRequest;
import com.lsh.atp.api.model.mis.QuerySaleRuleResponse;
import com.lsh.atp.api.model.mis.WarehouseDto;
import com.lsh.atp.api.service.mis.IQuerySaleRuleRPCService;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.model.area.Warehouse;
import com.lsh.atp.core.model.salerule.SaleRule;
import com.lsh.atp.core.service.area.WarehouseService;
import com.lsh.atp.core.service.mis.SaleRuleService;
import com.lsh.atp.core.util.AtpAssert;
import com.lsh.atp.core.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by jingyuan on 16/10/18.
 * 查询出货规则(根据区域)
 */
@Service(protocol = "dubbo")
public class QuerySaleRuleRPCService implements IQuerySaleRuleRPCService {

    private static Logger logger = LoggerFactory.getLogger(QuerySaleRuleRPCService.class);

    @Autowired
    private SaleRuleService saleRuleService;

    @Autowired
    private WarehouseService warehouseService;

    public QuerySaleRuleResponse querySaleRule(QuerySaleRuleRequest request) {
        QuerySaleRuleResponse response = new QuerySaleRuleResponse();
        String zoneCode = request.getZoneCode();    //区域码
        Integer type = request.getType();           //type
        type = type == null ? 1 : type;

        response.setZoneCode(zoneCode);
        response.setDataKey(new Date());
        try {

            this.validateParams(request);

            List<Warehouse> warehouses = warehouseService.getCommonWarehouse(zoneCode,type);
            AtpAssert.notEmpty(warehouses,ExceptionStatus.E2001027.getCode(),ExceptionStatus.E2001027.getMessage());
            Collections.sort(warehouses);

            List<WarehouseDto> warehouseDtos = new ArrayList<>(warehouses.size());
            for(Warehouse warehouse : warehouses){
                String subZoneCode = warehouse.getSubZoneCode();
                if(subZoneCode == null){
                    continue;
                }
                warehouseDtos.add(new WarehouseDto(warehouse.getDcId(),subZoneCode));
            }

            response.setSalerules(warehouseDtos);
            response.setStatus(0);
            response.setMeg("OK");
        } catch (BusinessException e) {
            response.setStatus(Integer.parseInt(e.getCode()));
            response.setMeg(e.getMessage());
            logger.error("查询出货规则,业务异常:" + e.getCode() + e.getMessage());
        } catch (Exception e) {
            response.setStatus(3001001);
            response.setMeg("服务端异常");
            logger.error("查询出货规则,服务端异常", e);
        }
        return response;
    }

    private void validateParams(QuerySaleRuleRequest request) {
        AtpAssert.notNull(request.getZoneCode(), ExceptionStatus.E1002001.getCode(),"zoneCode不能为空");
    }

}
