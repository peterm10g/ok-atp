package com.lsh.atp.service.hold;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.atp.api.model.Hold.HoldDetailQueryRequest;
import com.lsh.atp.api.model.Hold.HoldDetailQueryResponse;
import com.lsh.atp.api.model.baseVo.ItemDc;
import com.lsh.atp.api.service.hold.IHoldDatailQueryRPCService;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.model.hold.SkuHold;
import com.lsh.atp.core.service.hold.HoldService;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import com.lsh.atp.core.util.AtpAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang on 16/8/23.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
@Service(protocol = "dubbo")
public class HoldDetailQueryRPCService implements IHoldDatailQueryRPCService{

    private static Logger logger = LoggerFactory.getLogger(HoldDetailQueryRPCService.class);

    @Autowired
    private HoldService holdService;

    public HoldDetailQueryResponse queryHoldDetail(HoldDetailQueryRequest request) {

        HoldDetailQueryResponse response = new HoldDetailQueryResponse();
        response.setDataKey(new Date());

        int channel = Integer.parseInt(request.getChannel());
        String sequence = request.getSequence();
        List<ItemDc> items = request.getItems();

        response.setChannel(channel);
        response.setSequence(sequence);

        try{

            SkuHold skuHold = holdService.getSkuHoldByChannelAndSequence(channel,sequence);

            AtpAssert.notNull(skuHold,"2001013","预占信息不存在");
            response.setItems(holdService.queryHoldDetail(skuHold.getId(),items));
            response.setHoldStatus(skuHold.getStatus());
            response.setStatus(0);
            response.setMeg("success");

        }catch(BusinessException e){

            response.setStatus(Integer.parseInt(e.getCode()));
            response.setMeg(e.getMessage());
            logger.error("业务异常:" + e.getCode() + e.getMessage());

        }catch (Exception e){
            AsyncEvent.post(new EmailModel("sequence = " + sequence + e.toString(),"查询预占明细接口失败"));

            response.setStatus(3001001);
            response.setMeg("服务端异常");
            logger.error("服务端异常",e);

        }

        return response;
    }
}
