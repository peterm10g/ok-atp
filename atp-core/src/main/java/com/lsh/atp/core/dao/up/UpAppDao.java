package com.lsh.atp.core.dao.up;


import com.lsh.atp.core.model.up.UpApp;
import com.lsh.atp.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpAppDao {

	void insert(UpApp app);
	
	void update(UpApp app);
	
	UpApp getAppById(Integer id);

    Integer countApp(Map<String, Object> params);

    List<UpApp> getAppList(Map<String, Object> params);
	
}