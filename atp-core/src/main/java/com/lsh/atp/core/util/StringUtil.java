package com.lsh.atp.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project Name: lsh-atp
 * Created by jingyuan
 * Date: 16/10/20
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.core.util
 * desc: 字符串处理
 */
public class StringUtil {

    /**
     * 方法matchString:获取字符串str中,匹配正则表达式regx的字符串集合
     *
     * @param str
     * @param regx
     * @return java.util.List<java.lang.String>
     */
    public static List<String> matchString(String str, String regx) {
        Pattern p = Pattern.compile(regx);
        Matcher matcher = p.matcher(str);
        List<String> list = new ArrayList();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }
}
