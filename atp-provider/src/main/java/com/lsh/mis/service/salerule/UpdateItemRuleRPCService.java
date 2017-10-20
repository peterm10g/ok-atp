package com.lsh.mis.service.salerule;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.atp.api.model.baseVo.BaseResponse;
import com.lsh.atp.api.model.baseVo.ExceptionStatus;
import com.lsh.atp.api.model.mis.UpdateItemRuleRequest;
import com.lsh.atp.api.service.mis.IUpdateItemRuleRPCService;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.service.mis.SaleRuleService;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import com.lsh.atp.core.util.AtpAssert;
import com.lsh.base.common.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang
 * Date: 16/10/19
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.api.service.mis
 * desc:更新商品售卖规则RPC接口实现类
 */
@Service(protocol = "dubbo")
public class UpdateItemRuleRPCService implements IUpdateItemRuleRPCService{

    Logger logger = LoggerFactory.getLogger(UpdateItemRuleRPCService.class);

    @Autowired
    private SaleRuleService saleRuleService;

    public BaseResponse updateItemSaleRule(UpdateItemRuleRequest request) {
        BaseResponse response = new BaseResponse();
        response.setDataKey(new Date());

        try{
            request.setType(request.getType() == null ? 1 : request.getType());

            this.validationParams(request);
            if(request.getIsNew()!= null && request.getIsNew().intValue() == 1){    //新建商品
                this.saleRuleService.insertSaleWeightItem(request.getZoneCode(),request.getItemId(), request.getType());
            }else{  //已有商品
                this.saleRuleService.updateItemSaleRule(request.getZoneCode(),request.getSubZoneCode(),request.getItemId(),request.getDcs());
            }
            response.setStatus(0);
            response.setMeg("OK");
        }catch(BusinessException e){
            response.setStatus(Integer.parseInt(e.getCode()));
            response.setMeg(e.getMessage());
            logger.error("业务异常:" + e.getCode() + e.getMessage());
        }catch (Exception e){

            StringBuilder sb = new StringBuilder();
            sb.append(JsonUtils.obj2Json(request))
                    .append(e.toString());
            AsyncEvent.post(new EmailModel(sb.toString(),"更新出货规则失败"));

            response.setStatus(3001001);
            response.setMeg("服务端异常");
            logger.error("服务端异常",e);
        }
        return response;
    }

    private void validationParams(UpdateItemRuleRequest request) {
        String zoneCode = request.getZoneCode();
        AtpAssert.notNull(zoneCode, ExceptionStatus.E1001001.getCode(), "zoneCode不能为空");
        AtpAssert.notNull(request.getItemId(), ExceptionStatus.E1001001.getCode(), "itemId不能为空");
//        AtpAssert.notEmpty(request.getDcs(), ExceptionStatus.E1001001.getCode(), "dcs不能为空");
    }


}
