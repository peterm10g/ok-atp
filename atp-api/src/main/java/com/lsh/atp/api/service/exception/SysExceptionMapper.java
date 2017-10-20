package com.lsh.atp.api.service.exception;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.atp.api.model.baseVo.BaseResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Date;

/**
 * Project Name: lsh-atp
 * Created by peter
 * Date: 16/7/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.service.exception.
 * desc:系统级异常处理
 */
public class SysExceptionMapper implements ExceptionMapper<Throwable> {
    public  Response toResponse(Throwable throwable) {
        BaseResponse responseBaseVo = new BaseResponse();
        responseBaseVo.setDataKey(new Date());
        responseBaseVo.setStatus(ExceptionConstant.RES_CODE_500);
        StringBuilder msg;
        msg = new StringBuilder(throwable.getMessage());
        msg.append(" case by :");
        msg.append(throwable.getCause()!=null?throwable.getCause().getMessage():"");
        responseBaseVo.setMeg(msg.toString());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseBaseVo).type(ContentType.APPLICATION_JSON_UTF_8).build();
    }

}
