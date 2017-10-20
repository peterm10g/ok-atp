package com.lsh.atp.service.hold;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.atp.api.model.Hold.HoldRequest;
import com.lsh.atp.api.model.Hold.HoldResponse;
import com.lsh.atp.api.model.baseVo.ExceptionStatus;
import com.lsh.atp.api.model.baseVo.Item;
import com.lsh.atp.api.model.baseVo.PreHold;
import com.lsh.atp.api.service.hold.IHoldRpcService;
import com.lsh.atp.core.constant.BusiConstant;
import com.lsh.atp.core.constant.RedisKeyConstant;
import com.lsh.atp.core.dao.jedis.JedisStringDao;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.service.area.AreaService;
import com.lsh.atp.core.service.hold.HoldService;
import com.lsh.atp.core.service.inventory.InventoryLogicService;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import com.lsh.atp.core.util.AtpAssert;
import com.lsh.base.common.json.JsonUtils2;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Project Name: lsh-atp
 * Created by miaozhuang
 * Date: 16/8/19
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.service.hold.
 * desc:预占rpc服务
 */
@Service(protocol = "dubbo")
class HoldRpcServiceImpl implements IHoldRpcService {


    private static Logger logger = LoggerFactory.getLogger(HoldRpcServiceImpl.class);

    @Autowired
    private HoldService holdService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private InventoryLogicService inventoryLogicService;

    @Autowired
    private JedisStringDao jedisStringDao;


    /**
     * spring 注入 区域信息
     */
    @Resource(name = "zoneCodeMap")
    protected Map<String, String> zoneCodeMap;


    /**
     * Rpc 预占接口
     *
     * @param holdRequest 请求对象
     * @return 返回对象
     */
    public HoldResponse preHoldInventory(HoldRequest holdRequest) {
        logger.info("订单号 : " + holdRequest.getSequence() + ",开始调用预占接口");
        long t = System.currentTimeMillis();
        HoldResponse responseVo = new HoldResponse();
        responseVo.setDataKey(new Date());
        try {

            this.validateParam(holdRequest);

            //参数检查通过,调用预占接口
            logger.info("订单号 : " + holdRequest.getSequence() + ",开始skuPreHold方法");
            PreHold preHold = holdService.skuPreHold(holdRequest);
            logger.info("订单号 : " + holdRequest.getSequence() + ",结束skuPreHold方法");

            if (preHold != null) {

                if(StringUtils.isNotBlank(preHold.getResNo()) && preHold.getResNo().equals(String.valueOf(BusiConstant.SUB_HOLD_SUCCESS))){
                    responseVo.setStatus(Integer.parseInt(ExceptionStatus.SUCCESS01.getCode()));
                    responseVo.setMeg(ExceptionStatus.SUCCESS01.getMessage());
                }else{
                    responseVo.setStatus(Integer.parseInt(ExceptionStatus.SUCCESS.getCode()));
                    responseVo.setMeg(ExceptionStatus.SUCCESS.getMessage());
                }

                responseVo.setHoldId(preHold.getHoldNo());
                responseVo.setHoldStatus(preHold.getHoldStatus());
            } else {
                responseVo.setStatus(Integer.parseInt(ExceptionStatus.E2001004.getCode()));
                responseVo.setMeg(ExceptionStatus.E2001004.getMessage());
            }
            logger.info("订单号 : " + holdRequest.getSequence() + ",结束调用预占接口" + "time : " + (System.currentTimeMillis() - t));
        } catch (BusinessException e) {

            if (e.getCode().equals(ExceptionStatus.E2001016.getCode())) {

                List<String> skuList = new ArrayList<String>();
                skuList.add(e.getData());
                List<Item> items = new ArrayList<Item>();
                Item item = new Item();
                item.setItemId(Long.parseLong(e.getData()));
                items.add(item);
                items = inventoryLogicService.queryInventoryByAreaCode(holdRequest.getZoneCode(),holdRequest.getSaleAreaCode(), items,holdRequest.getSequence());

                if (items != null && !items.isEmpty()) {

                    responseVo.setItemList(items);

                }

                responseVo.setSkuIdList(skuList);
            }

            responseVo.setStatus(Integer.parseInt(e.getCode()));
            responseVo.setMeg(e.getMessage());

            logger.error("自定义异常信息: " + e.getCode() + ":" + e.getMessage());
        } catch (Exception e) {
            //异步发送邮件
            StringBuilder content = new StringBuilder();
            content.append("参数为 : ").append(JsonUtils2.obj2Json(holdRequest))
                   .append("\n").append("错误信息为 : ").append("\n").append(e.getMessage());
            AsyncEvent.post(new EmailModel(content.toString(),"扣减接口异常"));

            responseVo.setStatus(Integer.parseInt(ExceptionStatus.E3001001.getCode()));
            responseVo.setMeg(ExceptionStatus.E3001001.getMessage());
            logger.error(ExceptionStatus.E3001001.getMessage(), e);

        }


        return responseVo;
    }

    /**
     * 参数校验
     *
     * @param holdRequest 请求对象
     */
    private void validateParam(HoldRequest holdRequest) {
        long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        //预占时间检查
        if (holdRequest.getHoldEndTime() == null || holdRequest.getHoldEndTime() < currentTime) {
            throw new BusinessException(ExceptionStatus.E2001019.getCode(), ExceptionStatus.E2001019.getMessage());
        }

        //非空参数检查
        AtpAssert.notNull(holdRequest.getZoneCode(),ExceptionStatus.E1002001.getCode(),"zoneCode 不能为空");
        AtpAssert.notNull(holdRequest.getChannel(), ExceptionStatus.E1002001.getCode(), "channel 不能为空");
        AtpAssert.notNull(holdRequest.getSequence(), ExceptionStatus.E1002001.getCode(), "sequence 不能为空");
        AtpAssert.notNull(holdRequest.getIsDecrease(),ExceptionStatus.E1002001.getCode(),"isDecrease 不能为空");
        AtpAssert.notEmpty(holdRequest.getItems(), ExceptionStatus.E1002002.getCode(), "items不能为空");
        //区域码转换 校验
        this.zoneCodeValidate(holdRequest);
    }

    /**
     * 区域码,区域id,验证与获取
     *
     * @param holdRequest 请求对象
     */
    private void zoneCodeValidate(HoldRequest holdRequest) {

        if (StringUtils.isBlank(holdRequest.getZoneCode())) {       //如果zoneCode为空,取areaId转为zoneCode
            //如果areaId为空,则抛异常
            if (StringUtils.isBlank(holdRequest.getAreaCode())) {
                throw new BusinessException(ExceptionStatus.E2001010.getCode(), "zone_code,area_code必传一个");
            }

            String zoneCode = this.getZoneCodeFromRedisByAreaCode(holdRequest.getAreaCode());

            if (StringUtils.isBlank(zoneCode)) {

                zoneCode = areaService.getZoneCodeByDistinct(holdRequest.getAreaCode());

                this.setZoneCode2redisByAreaCode(holdRequest.getAreaCode(), zoneCode);

            }

            if (StringUtils.isBlank(zoneCode)) {

                throw new BusinessException(ExceptionStatus.E2001010.getCode(), "zone_code 不存在");
            }

            holdRequest.setZoneCode(zoneCode);

            //holdRequest.setAreaCode(areaCodeService.getZoneId(zoneCode));
            holdRequest.setAreaCode(zoneCodeMap.get(zoneCode));

        } else {
            String zoneId = zoneCodeMap.get(holdRequest.getZoneCode());
            //areaCodeService.getZoneId(holdRequest.getZoneCode());
            if (StringUtils.isBlank(zoneId)) {
                throw new BusinessException(ExceptionStatus.E2001010.getCode(), "zone_code 不存在");
            }
            holdRequest.setAreaCode(zoneId);
        }

    }

    /**
     * 通过redis获取区域码
     *
     * @param areaCode 地址码
     * @return 区域码
     */
    private String getZoneCodeFromRedisByAreaCode(String areaCode) {

        String areaKey = MessageFormat.format(RedisKeyConstant.ZONE_AREA, areaCode);

        String zoneCode = null;

        try {
            zoneCode = jedisStringDao.get(areaKey);

            logger.info("redis get zoneCode is : " + zoneCode);
        } catch (Exception e) {
            logger.error("redis Exception");
        }

        return zoneCode;
    }

    /**
     * @param areaCode 地址码
     * @param zoneCode 区域码
     */
    private void setZoneCode2redisByAreaCode(String areaCode, String zoneCode) {

        String areaKey = MessageFormat.format(RedisKeyConstant.ZONE_AREA, areaCode);

        logger.info(" ************  areaKey : zoneCode " + areaKey + ":" + zoneCode);

        try {
            jedisStringDao.set(areaKey, zoneCode);
        } catch (Exception e) {
            logger.error("set redis Exception");
        }
    }

}
