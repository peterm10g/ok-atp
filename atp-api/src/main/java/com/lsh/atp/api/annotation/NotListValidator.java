package com.lsh.atp.api.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by zhangqiang on 16/8/2.
 */
public class NotListValidator implements ConstraintValidator<NotList,String>{



    public void initialize(NotList constraintAnnotation) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if("".equals(value) || value == null){
            return false;
        }
        String s  =  "";
        s.charAt(34);

        value = value.trim();

        String template = context.getDefaultConstraintMessageTemplate();

        System.out.println(template);

        if(value.charAt(0) != '[' || value.charAt(value.length() - 1) != ']'){
            return false;
        }
        return false;
    }
}
