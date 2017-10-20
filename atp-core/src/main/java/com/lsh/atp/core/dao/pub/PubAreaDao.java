package com.lsh.atp.core.dao.pub;


import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.pub.PubArea;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PubAreaDao {

	void insert(PubArea pubArea);
	
	void update(PubArea pubArea);
	
	PubArea getPubAreaById(Integer id);

    Integer countPubArea(Map<String, Object> params);

    List<PubArea> getPubAreaList(Map<String, Object> params);
	
}