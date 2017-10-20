package com.lsh.mis.service.salerule;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.atp.api.model.baseVo.BaseResponse;
import com.lsh.atp.api.model.mis.UpdateItemRuleRequest;
import com.lsh.atp.api.service.mis.IUpdateItemRuleRestService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang
 * Date: 16/10/19
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.api.service.mis
 * desc:更新商品售卖规则Rest接口实现类
 */
@Service(protocol = "rest")
@Path("mis")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public class UpdateItemRuleRestService implements IUpdateItemRuleRestService {

    @Autowired
    private UpdateItemRuleRPCService updateItemRuleRPCService;

    @Path("updateSaleRule")
    @POST
    public BaseResponse updateItemSaleRule(UpdateItemRuleRequest request) {
        return this.updateItemRuleRPCService.updateItemSaleRule(request);
    }
}
