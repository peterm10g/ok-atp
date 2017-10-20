package com.lsh.atp.service.inventory;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.atp.api.model.Hold.HoldResponse;
import com.lsh.atp.api.model.baseVo.ExceptionStatus;
import com.lsh.atp.api.model.inventory.RestoreInventoryRequest;
import com.lsh.atp.api.service.inventory.IInventoryRestoreRPCService;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.service.inventory.InventoryLogicService;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import com.lsh.atp.core.util.AtpAssert;
import com.lsh.base.common.json.JsonUtils2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang
 * Date: 16/8/22
 * Time: 16/8/22
 * 北京链商电子商务有限公司
 * Package name:com.lsh.atp.service.inventory
 * desc:还原库存RPC接口实现类
 */
@Service(protocol = "dubbo")
public class InventoryRestoreRPCService implements IInventoryRestoreRPCService{

    private Logger logger = LoggerFactory.getLogger(InventoryRestoreRPCService.class);

    @Autowired
    private InventoryLogicService inventoryLogicService;


    public HoldResponse restoreInventory(RestoreInventoryRequest request) {
        //返回对象
        HoldResponse response = new HoldResponse();
        response.setDataKey(new Date());

        try{
            this.validateParams(request);
            response.setHoldId(inventoryLogicService.restoreInventory(request));

            response.setStatus(0);
            response.setMeg("OK");

        }catch (BusinessException e) {
            response.setStatus(Integer.parseInt(e.getCode()));
            response.setMeg(e.getMessage());
            logger.info("业务异常:" + e.getCode() + e.getMessage());
        } catch (Exception e) {
            response.setStatus(3001001);
            response.setMeg("服务端异常");
            logger.error("服务端异常", e);

            //异步发送邮件
            StringBuilder content = new StringBuilder();
            content.append("参数为 : ").append(JsonUtils2.obj2Json(request))
                    .append("\n").append("错误信息为 : ").append("\n").append(e.getMessage());
            AsyncEvent.post(new EmailModel(content.toString(),"还原接口异常"));
        }



        return response;
    }

    private void validateParams(RestoreInventoryRequest request) {
        AtpAssert.notNull(request.getChannel(), ExceptionStatus.E1002001.getCode(),"channel不能为空");
        AtpAssert.notNull(request.getSequence(), ExceptionStatus.E1002001.getCode(),"Sequence不能为空");
    }
}
