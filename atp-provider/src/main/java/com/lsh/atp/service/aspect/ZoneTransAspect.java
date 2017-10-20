package com.lsh.atp.service.aspect;

import com.lsh.atp.api.model.baseVo.ZoneTransform;
import com.lsh.atp.core.util.ZoneResolver;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by zhangqiang on 17/6/21.
 */
@Aspect
@Component
public class ZoneTransAspect {

    private static final Logger logger = LoggerFactory.getLogger(ZoneTransAspect.class);

    private static String DEFAULT_CHANNEL = "1";

    /**
     * REST方法默认调用RPC方法,这里只定义了拦截了RPC方法
     */
    @Pointcut("execution(* com.lsh.atp.service.*.*RPCService*.*(..)) || execution(* com.lsh.mis.service.*.*RPCService*.*(..)) || execution(* com.lsh.atp.service.*.*RpcService*.*(..))")
    public void declareJointPointExpression(){}

    @Around("declareJointPointExpression()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String zoneCode = this.transformBeforeInvoke(args);

        Object result = joinPoint.proceed(args);

        this.transformAfterInvoke(result,zoneCode);
        return result;
    }

    /**
     * 执行方法前转换zoneCode
     * @param args 方法参数
     * @return 原zoneCode
     */
    private String transformBeforeInvoke(Object[] args){
        String zoneCode = null; //原zoneCode
        for(Object obj : args){
            if(obj instanceof ZoneTransform){
                ZoneTransform zoneTransform = (ZoneTransform) obj;
                zoneCode = zoneTransform.getZoneCode();
                logger.info("参数需要转换zoneCode,zoneCode = " + zoneCode);
                String channel = zoneTransform.getChannel();
                if(channel == null){
                    channel = DEFAULT_CHANNEL;
                }
                String realZoneCode = ZoneResolver.getRealZoneCode(channel, zoneCode);
                logger.info("转换zoneode完成, realZoneCode = " + realZoneCode);
                zoneTransform.setZoneCode(realZoneCode);
            }
        }
        return zoneCode;
    }

    /**
     * 执行方法后转换zoneCode
     * @param result 方法返回
     * @param zoneCode 原zoneCode
     */
    private void transformAfterInvoke(Object result,String zoneCode) {
        if(result instanceof  ZoneTransform){
            ZoneTransform zoneTransform = (ZoneTransform) result;
            zoneTransform.setZoneCode(zoneCode);
        }
    }

}
