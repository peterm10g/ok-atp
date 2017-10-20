package com.lsh.atp.core.dao.hold;

import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.hold.HoldLog;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface HoldLogDao {
	/**
	 * 新增一条预占日志(HOLDLog)
	 *
	 * @param holdLog holdLog对象
	 **/
	void insert(HoldLog holdLog);

	/**
	 * 更新预占日志(HOLDLOG)
	 *
	 * @param holdLog holdLog对象
	 **/
	void update(HoldLog holdLog);

	/**
	 * 根据id查询一条HoldLog记录
	 *
	 * @param id id
	 *
	 * @return HoldLog对象
	 * **/
	HoldLog getHoldLogById(Integer id);

	/**
	 * 根据条件map查询符合条件的数据总条数
	 *
	 * @param params map
	 *
	 * @return 符合条件的数量
	 * **/
    Integer countHoldLog(Map<String, Object> params);


	/**
	 * 根据条件map查询符合的HoldLog集合
	 *
	 * @param params map
	 *
	 * @return HoldLog对象集合
	 * **/
    List<HoldLog> getHoldLogList(Map<String, Object> params);
	
}