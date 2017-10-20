package com.lsh.atp.core.common;

import com.lsh.atp.api.model.baseVo.BaseResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;

/**
 * Project Name: lsh-atp
 * Created by miaozhuang on 16/7/15.
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.service.common
 * desc: 请求头信息校验
 */
public class HttpRequestFilter implements ContainerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(HttpRequestFilter.class);

    /**
     * 请求头信息校验
     * @param context 请求上下文
     * @throws IOException 异常
     */
    public void filter(ContainerRequestContext context) throws IOException {
        MultivaluedMap<String, String> map = context.getHeaders();
        String apiversion = map.getFirst("api-version");
        String random = map.getFirst("random");
        String platform = map.getFirst("platform");
//      String secretType = map.getFirst("secret-type");
//      String sign = map.getFirst("sign");

        logger.info("api-version:random:platform = " + apiversion + ":" + random + ":" + platform);

        if (StringUtils.isAnyBlank(apiversion, random, platform)) {
            context.abortWith(bulidHeaderCheckNotPassResponse());
//            return;
        }

    }

    /**
     * 异常信息组装
     * @return Response
     */
    private Response bulidHeaderCheckNotPassResponse() {

        return Response.ok().status(Response.Status.OK).entity(new BaseResponse(1001001, "头信息参数错误", new Date())).build();
    }
}
