package com.lsh.atp.core.dao.area;

import com.lsh.atp.core.dao.BaseDao;
import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.area.Area;
import org.apache.ibatis.annotations.Param;

/**
 * Created by huangdong on 16/7/7.
 */
@MyBatisRepository
public interface AreaDao extends BaseDao<Area, Integer> {
    String getZonecodeByDistrict(@Param("districtCode") String districtCode);
}