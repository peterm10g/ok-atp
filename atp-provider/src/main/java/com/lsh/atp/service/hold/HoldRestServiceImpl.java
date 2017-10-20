package com.lsh.atp.service.hold;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.atp.api.model.Hold.HoldRequest;
import com.lsh.atp.api.model.Hold.HoldResponse;
import com.lsh.atp.api.model.baseVo.BaseResponse;
import com.lsh.atp.api.model.baseVo.ExceptionStatus;
import com.lsh.atp.api.service.hold.HoldRestService;
import com.lsh.atp.core.service.email.SendEmailService;
import com.lsh.atp.core.service.hold.HoldService;
import com.lsh.atp.core.service.inventory.InventorySyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * Project Name: lsh-atp
 * Created by miaozhuang
 * Date: 16/7/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.service.hold.
 * desc:预占rest服务
 */
@Service(protocol = "rest")
@Path("inventory")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8})
public class HoldRestServiceImpl implements HoldRestService {

    private static Logger logger = LoggerFactory.getLogger(HoldRestServiceImpl.class);

    @Autowired
    private HoldService holdService;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private HoldRpcServiceImpl holdRpcService;

    @Autowired
    private InventorySyncService inventorySyncService;


    /**
     * 库存预占接口
     *
     * @param holdRequestVo 请求对象
     * @return HoldResponseVo
     */
    @POST
    @Path("prehold")
    public HoldResponse preHold(HoldRequest holdRequestVo) {

        return holdRpcService.preHoldInventory(holdRequestVo);

    }


    /**
     * 还原rest接口
     * @return BaseResponse
     */
    @POST
    @Path("restoreHold")
    public BaseResponse restoreInventoryByHold(){

        BaseResponse vo = new BaseResponse();

        try {

            sendEmailService.sendAsyn("异步邮件发送","java版异步邮件发送",new String[]{"aop@lsh123.com"});

//            List<SkuHold> skuList = holdService.restoreQueryJop();

//            if (skuList != null && !skuList.isEmpty()) {
//                logger.info("skuList size is :" + skuList.size());
//                for (SkuHold skuHold : skuList) {
//                    holdService.restoreDetailJop(skuHold);
//                }
//            }

//            Map<String,Map<String,Long>> resmap = inventorySyncService.wumartCode2lshCode(null);//inventorySyncService.getSyntime()
//
//            logger.info("size is 1 " + resmap.get("1").size());
//            logger.info("size is 1 " + resmap.get("3").size());
//            logger.info("size is 1 " + resmap.get("4").size());

//            Map<String,Map<Long, Integer>> resMap = inventorySyncService.getMaxSaleUnitByTime(null);//inventorySyncService.getSynSkuListTime()
//
//            logger.info("size is " + resMap.size());
//
//            logger.info("resMap is " + resMap.get("1000").size());


            vo.setStatus(Integer.parseInt(ExceptionStatus.SUCCESS.getCode()));
            vo.setMeg(ExceptionStatus.SUCCESS.getMessage());

        }catch (Exception e){

            vo.setStatus(Integer.parseInt(ExceptionStatus.E3001001.getCode()));
            vo.setMeg(ExceptionStatus.E3001001.getMessage());
            logger.error(ExceptionStatus.E3001001.getMessage(),e);
            logger.error("还原异常",e);

        }

        return vo;
    }


}
