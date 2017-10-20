package com.lsh.atp.core.service.area;

import com.lsh.atp.core.dao.area.AreaAddressDao;
import com.lsh.atp.core.dao.area.AreaDao;
import com.lsh.atp.core.model.area.Area;
import com.lsh.atp.core.model.area.AreaAddress;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by panxudong on 16/7/8.
 */
@Component
public class AreaCodeService {

    @Autowired
    private AreaAddressDao areaAddressDao;

    @Autowired
    private AreaDao areaDao;


    /**
     * 根据areaCode查询area_id
     *
     * @param areaCode
     *
     * @return area_id
     *
     * **/
    public String getAreaCode(String areaCode) {
        //获取Address
        AreaAddress areaAddress = new AreaAddress();
        areaAddress.setDistrictCode(areaCode);

        List<AreaAddress> areaAddressList = areaAddressDao.findList(areaAddress);

        if(areaAddressList.size() <= 0 || areaAddressList.size() > 1) {
            return null;
        }


        areaAddress = areaAddressList.get(0);

        return String.valueOf(areaAddress.getAreaId());
    }

    /**
     * 获取区域码
     * @param areaId
     * @return
     */
    public String getZoneCode(Integer areaId) {
        if(areaId == null){
            return null;
        }
        //获取Address
        Area area = new Area();
        area.setId(areaId);

        Area dbarea = areaDao.findOne(area);
        if(dbarea == null){
            return null;
        }

        return dbarea.getCode();
    }

    /**
     * 获取区域主键
     * @param
     * @return
     */
    public String getZoneId( String zoneCode) {
        if(StringUtils.isBlank(zoneCode)){
            return null;
        }
        //获取Address
        Area area = new Area();
        area.setCode(zoneCode);

        Area dbarea = areaDao.findOne(area);
        if(dbarea == null){
            return null;
        }

        return String.valueOf(dbarea.getId());
    }
}
