package com.lsh.atp.core.dao.up;


import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.up.UpModel;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpModelDao {

	void insert(UpModel model);
	
	void update(UpModel model);
	
	UpModel getModelById(Integer id);

    Integer countModel(Map<String, Object> params);

    List<UpModel> getModelList(Map<String, Object> params);
	
}