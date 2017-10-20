package com.lsh.atp.core.dao.salerule;

import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.area.SupplyDc;
import com.lsh.atp.core.model.salerule.SaleWeightItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * Created by zhangqiang on 16/12/7.
 */
@MyBatisRepository
public interface SaleWeightItemDao {

    /*-----------  查询方法  ------------------*/

    /**
     * 查询权重大于0的supplyDC
     * @param itemId
     * @param zoneCode
     * @param subZoneCode 如果为null,则查的是全zoneCode
     * @return
     */
    List<SupplyDc> getSupplyWeight(@Param("itemId")Long itemId, @Param("zoneCode") String zoneCode, @Param("subZoneCode")String subZoneCode);

    List<SaleWeightItem> getSupplyWeightWithWeight(@Param("itemId")Long itemId, @Param("zoneCode") String zoneCode,  @Param("subZoneCode")String subZoneCode);

    Double getWeight(@Param("itemId")Long itemId, @Param("zoneCode") String zoneCode,@Param("subZoneCode")String subZoneCode, @Param("supplyId")Integer supplyId, @Param("dc")String dc);


    int deleteByPrimaryKey(Long id);

    int insert(SaleWeightItem record);

    int insertSelective(SaleWeightItem record);

    SaleWeightItem selectByPrimaryKey(Long id);

    SaleWeightItem selectBySaleWeightItem(SaleWeightItem record);

    List<SaleWeightItem> selectDistItem(@Param("zoneCode") String zoneCode);

    int updateByPrimaryKeySelective(SaleWeightItem record);

    int updateByPrimaryKey(SaleWeightItem record);



    int deleteByItem(@Param("itemId") Long itemId, @Param("zoneCode") String zoneCode, @Param("subZoneCode")String subZoneCode);



}
