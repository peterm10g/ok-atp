package com.lsh.atp.core.dao.area;

import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.area.Warehouse;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface WarehouseDao {

	void insert(Warehouse warehouse);
	
	void update(Warehouse warehouse);
	
	Warehouse getWarehouseById(Long id);

    Integer countWarehouse(Map<String, Object> params);

    List<Warehouse> getWarehouseList(Map<String, Object> params);

	Warehouse getWarehoseByDc(@Param("dc") String dc);

	List<String> getDcWeight(@Param("supply")Integer supply);

	List<Warehouse> getDcWeightWithWeight(@Param("supply")Integer supply);

	Integer getSupplyIdByDc(@Param("dcId")String dcId);

	/**
	 * 根据dcReal 与 supply 获取 DC——id  ofc履约用 兼容
	 */
	String getDcId(@Param("dcReal")String dcReal, @Param("supply") Integer supply);

	List<Warehouse> getWarehouseByType(@Param("zoneCode") String zoneCode, @Param("type") Integer type);

	List<Warehouse> getWarehouseBySupply(@Param("zoneCode") String zoneCode, @Param("supply")Integer supply);
}