package com.lsh.atp.core.service.area;

import com.lsh.atp.core.dao.area.AreaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhangqiang on 16/8/19.
 */
@Component
public class AreaService {
    @Autowired
    private AreaDao areaDao;

    /**
     * 根据行政码获取区域码
     *
     * @param districtCode
     *
     * @return
     * **/
    public String getZoneCodeByDistinct(final String districtCode){
        return areaDao.getZonecodeByDistrict(districtCode);
    }
}
