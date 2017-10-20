package com.lsh.atp.core.dao.pub;


import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.pub.PubConfigData;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PubConfigDataDao {

	void insert(PubConfigData pubConfigData);
	
	void update(PubConfigData pubConfigData);
	
	PubConfigData getPubConfigDataById(Integer id);

    Integer countPubConfigData(Map<String, Object> params);

    List<PubConfigData> getPubConfigDataList(Map<String, Object> params);
	
}