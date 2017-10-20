package com.lsh.mis.service.salerule;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.atp.api.model.mis.CurrentDcGetRequest;
import com.lsh.atp.api.model.mis.CurrentDcGetResponse;
import com.lsh.atp.api.service.mis.ICurrentDcRestService;
import com.lsh.atp.core.enums.Zone;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.service.redis.currentdc.CurrentDcRedisService;
import com.lsh.base.common.json.JsonUtils2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * Created by zhangqiang on 17/7/28.
 */
@Service(protocol = "rest")
@Path("mis")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public class CurrentDcRestService implements ICurrentDcRestService {

    private static final Logger logger = LoggerFactory.getLogger(CurrentDcRestService.class);

    @Autowired
    private CurrentDcRPCService currentDcRPCService;

    @POST
    @Consumes()
    @Path("currentDc/get")
    @Override
    public CurrentDcGetResponse getCurrentDc(CurrentDcGetRequest request) {
        return currentDcRPCService.getCurrentDc(request);
    }



}
