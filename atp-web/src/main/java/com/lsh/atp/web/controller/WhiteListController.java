package com.lsh.atp.web.controller;

import com.lsh.atp.web.domain.ItemWhite;
import com.lsh.atp.web.service.WhiteListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Properties;

/**
 * Created by zhangqiang on 17/6/14.
 */
@Controller
public class WhiteListController {

    private static final Logger logger = LoggerFactory.getLogger(WhiteListController.class);

    @Autowired
    private WhiteListService whiteListService;

    @RequestMapping("/whiteList")
    public ModelAndView show(){
        return new ModelAndView("whiteList").addObject(new ItemWhite());
    }

    @RequestMapping("/whiteList/add")
    @ResponseBody
    public boolean addWhiteList(ItemWhite itemWhite){
        boolean isSuccess;
        try{
            isSuccess = this.whiteListService.addWhiteList(itemWhite);
        }catch (Throwable e){
            logger.error("error",e);
            isSuccess = false;
        }

        return isSuccess;
    }

    public static void main(String[] args){
        DefaultListableBeanFactory lbf = new DefaultListableBeanFactory();
        Properties p = new Properties();
        p.setProperty("test.(class)", ItemWhite.class.getName());
        p.setProperty("test.zoneCode", "1000");
        int count = (new PropertiesBeanDefinitionReader(lbf)).registerBeanDefinitions(p);
        ItemWhite tb = (ItemWhite) lbf.getBean("test");
        System.out.println(tb.getZoneCode());

    }

}
