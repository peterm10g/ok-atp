package com.lsh.atp.core.dao.up;


import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.up.UpRule;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpRuleDao {

	void insert(UpRule rule);
	
	void update(UpRule rule);
	
	UpRule getRuleById(Integer id);

    Integer countRule(Map<String, Object> params);

    List<UpRule> getRuleList(Map<String, Object> params);
	
}