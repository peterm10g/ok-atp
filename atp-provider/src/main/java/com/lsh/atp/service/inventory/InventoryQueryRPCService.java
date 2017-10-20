package com.lsh.atp.service.inventory;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.atp.api.model.baseVo.ExceptionStatus;
import com.lsh.atp.api.model.baseVo.Item;
import com.lsh.atp.api.model.inventory.QueryInventory;
import com.lsh.atp.api.model.inventory.QueryInventoryRequest;
import com.lsh.atp.api.service.inventory.IInventoryQueryRPCService;
import com.lsh.atp.core.enums.Zone;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.service.inventory.InventoryLogicService;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import com.lsh.atp.core.util.AtpAssert;
import com.lsh.base.common.json.JsonUtils2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang
 * Date: 16/8/19
 * Time: 16/8/19
 * 北京链商电子商务有限公司
 * Package name:com.lsh.atp.service.inventory
 * desc:查询库存RPC接口实现类
 */
@Service(protocol = "dubbo")
public class InventoryQueryRPCService implements IInventoryQueryRPCService{

    private static Logger logger = LoggerFactory.getLogger(InventoryQueryRPCService.class);

    @Autowired
    private InventoryLogicService inventoryLogicService;


    public QueryInventory queryInventory(QueryInventoryRequest request) {
        final String uuid = UUID.randomUUID().toString();
        final QueryInventory response = new QueryInventory(); //返回对象
        response.setDataKey(new Date());
        response.setChannel(request.getChannel());

        try{

            this.validationParams(request);

            List<Item> items = request.getItems();              //请求参数中的items
            logger.info(uuid + " 查询库存,条数为" + items.size());
            String zoneCode = request.getZoneCode();          //区域code
            String subZoneCode = request.getSaleAreaCode();
            if(subZoneCode == null){
                subZoneCode = Zone.parse(zoneCode).getDefaultSubZoneCode();
            }

            items = inventoryLogicService.queryInventoryByAreaCode(zoneCode,subZoneCode,items,uuid);
            response.setItems(items);
            response.setStatus(0);
            response.setMeg("OK");

        }catch(BusinessException e){
            response.setStatus(Integer.parseInt(e.getCode()));
            response.setMeg(e.getMessage());
            logger.error("业务异常:" + e.getCode() + e.getMessage());
        }catch (Exception e){

            response.setStatus(3001001);
            response.setMeg("服务端异常");
            logger.error("服务端异常",e);

            //异步发送邮件
            StringBuilder content = new StringBuilder();
            content.append("UUID : ").append(uuid)
                    .append("参数为 : ").append(JsonUtils2.obj2Json(request))
                    .append("\n").append("错误信息为 : ").append("\n").append(e.getMessage());
            AsyncEvent.post(new EmailModel(content.toString(),"查询接口异常"));
        }
        logger.info(uuid + " 返回为: " + JSON.toJSONString(response));
        return response;
    }

    private void validationParams(QueryInventoryRequest request) {
        AtpAssert.notEmpty(request.getItems(), ExceptionStatus.E1001001.getCode(),"items不能为空");
        for(Item item : request.getItems()){
            AtpAssert.notNull(item.getItemId(), ExceptionStatus.E1001001.getCode(),"item_id不能为空");
        }
        AtpAssert.notNull(request.getZoneCode(),ExceptionStatus.E1001001.getCode(),"zoneCode不能为空");
    }
}
