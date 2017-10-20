package com.lsh.atp.core.util;

import com.lsh.atp.core.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang on 16/8/19.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
public abstract class AtpAssert {
    /**
     * Assert that an string is not {@code null}.
     * <pre class="code">Assert.isNull(value, "The value must be null");</pre>
     * @param string the object to check
     * @param code 错误码
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is not {@code null}
     */
    public static void notNull(String string,String code, String message) {
        if (StringUtils.isBlank(string)) {
            throw new BusinessException(code,message);
        }
    }

    /**
     * Assert that an obj is not {@code empty}.
     *
     * @param obj 判断参数
     * @param code 错误码
     * @param message 异常信息
     */
    public static void notNull(Object obj,String code, String message) {
        if (obj == null) {
            throw new BusinessException(code,message);
        }
    }

    /**
     *  Assert that an list is not {@code empty}.
     *
     * @param list 判断参数
     * @param code 错误码
     * @param message 异常信息
     * **/
    public static void notEmpty(Collection list, String code, String message){
        if(list == null || list.size() == 0){
            throw new BusinessException(code,message);
        }
    }

    /**
     *  Assert that an Long is not {@code empty}.
     *
     * @param value 判断参数
     * @param code 错误码
     * @param message 异常信息
     * **/
    public static void notLessThanZero(Long value, String code, String message){
        if(value == null || value <= 0){
            throw new BusinessException(code,message);
        }
    }




}
