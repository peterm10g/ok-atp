package com.lsh.atp.web.controller;

import com.lsh.atp.core.task.model.InventoryRecord;
import com.lsh.atp.web.domain.InventoryRecodeQuery;
import com.lsh.atp.web.service.InventoryRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangqiang on 17/5/12.
 */
@Controller
public class InventoryRecordController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryRecordController.class);

    @Autowired
    private InventoryRecordService inventoryRecordService;

    @RequestMapping("/")
    public ModelAndView show(){
        InventoryRecodeQuery query = new InventoryRecodeQuery();
        return new ModelAndView("inventoryRecord").addObject(query).addObject("inventoryRecords",new ArrayList<>(0));
    }

    @RequestMapping("/queryInventoryRecord")
    public ModelAndView queryInventoryRecord(InventoryRecodeQuery query){
        logger.info(query.getItemId() + " " + query.getRecordType() + " "+query.getDc() );
        List<InventoryRecord> list = null;
        try{
            list = inventoryRecordService.search(query);
            for(InventoryRecord inventoryRecord : list){
                Long time = inventoryRecord.getUpdatedAt();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(time);
                String dateFormat = simpleDateFormat.format(date);
                inventoryRecord.setUpdatedFormat(dateFormat);
            }
        }catch (Throwable e){
            logger.error("",e);
        }finally {
            logger.info("结果为 " + list.size());
        }

        return new ModelAndView("inventoryRecord").addObject("inventoryRecords",list);
    }
}
