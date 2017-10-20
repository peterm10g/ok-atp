package com.lsh.atp.core.dao.salerule;

import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.salerule.SaleRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface SaleRuleDao {
    int deleteByPrimaryKey(Long id);

    int insert(SaleRule record);

    int insertSelective(SaleRule record);

    SaleRule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SaleRule record);

    int updateByPrimaryKey(SaleRule record);

    List<SaleRule> selectByZonecode(String zoneCode);

    List<SaleRule> selectByZonecodeAndType(@Param("zoneCode")String zoneCode, @Param("type") Integer type);

    /**
     * 查询zonecode item确定的唯一出货规则
     * @param ruleCode
     * @param zoneCode
     * @return
     */
    String getSupplys( @Param("zoneCode") String zoneCode,@Param("ruleCode") Long ruleCode);

    // TODO: 17/7/25 此方法查询太多,存入redis做缓存。key supplysStr : saleRule Json
    SaleRule selectBySupplys(@Param("zoneCode")String zoneCode, @Param("supplys")String supplys);
}