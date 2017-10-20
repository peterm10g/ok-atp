package com.lsh.atp.api.service.exception;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.atp.api.model.baseVo.BaseResponse;
import com.lsh.base.common.exception.BizCheckedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Date;

/**
 * Project Name: lsh-atp
 * Created by fuhao
 * Date: 16/7/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.api.service.exception.
 * desc:类功能描述
 */
public class BizExceptionMapper implements ExceptionMapper<BizCheckedException> {
    public Response toResponse(BizCheckedException ex) {
        BaseResponse responseBaseVo = new BaseResponse();
        responseBaseVo.setDataKey(new Date());
        responseBaseVo.setStatus(ex.getCode()!=null?Integer.valueOf(ex.getCode()): ExceptionConstant.RES_CODE_500);
        /* StringBuffer msg = new StringBuffer();
         msg.append(ex.getMessage());
         msg.append(" case by :");
         msg.append(ex.getExceptionStackInfo());
         responseBaseVo.setMsg(msg.toString());*/ //todo 业务异常不抛出异常堆栈
        responseBaseVo.setMeg(ex.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseBaseVo).type(ContentType.APPLICATION_JSON_UTF_8).build();
    }
}
