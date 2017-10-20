package com.lsh.mis.service.salerule;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.atp.api.model.mis.CurrentDcGetRequest;
import com.lsh.atp.api.model.mis.CurrentDcGetResponse;
import com.lsh.atp.api.service.mis.ICurrentDcRPCService;
import com.lsh.atp.core.enums.Zone;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.service.redis.currentdc.CurrentDcRedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by zhangqiang on 17/8/3.
 */
@Service(protocol = "dubbo")
public class CurrentDcRPCService implements ICurrentDcRPCService {

    private static final Logger logger = LoggerFactory.getLogger(CurrentDcRPCService.class);

    @Autowired
    private CurrentDcRedisService currentDcRedisService;

    @Override
    public CurrentDcGetResponse getCurrentDc(CurrentDcGetRequest request) {
        CurrentDcGetResponse response = new CurrentDcGetResponse();
        response.setDataKey(new Date());

        String zoneCode = request.getZoneCode();
        Long itemId = request.getItemId();
        try {
            if(StringUtils.isBlank(request.getSaleAreaCode())){
                request.setSaleAreaCode(Zone.parse(zoneCode).getDefaultSubZoneCode());
            }
            String currentDc =  currentDcRedisService.getCurrentDc(itemId,zoneCode,request.getSaleAreaCode());
            response.setStatus(0);
            response.setMeg("success");
            response.setCurrentDc(currentDc);
        } catch (BusinessException e) {
            response.setStatus(Integer.parseInt(e.getCode()));
            response.setMeg(e.getMessage());
            logger.error("查询当前售卖仓库异常," + e.getCode() + e.getMessage());
        } catch (Exception e) {
            response.setStatus(3001001);
            response.setMeg("服务端异常");
            logger.error("查询库存,服务端异常:", e);
        }
        return response;
    }
}
