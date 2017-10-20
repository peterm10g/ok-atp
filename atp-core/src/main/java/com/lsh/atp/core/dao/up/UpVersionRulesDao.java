package com.lsh.atp.core.dao.up;


import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.up.UpVersionRules;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpVersionRulesDao {

	void insert(UpVersionRules versionRules);
	
	void update(UpVersionRules versionRules);
	
	UpVersionRules getVersionRulesById(Long id);

    Integer countVersionRules(Map<String, Object> params);

    List<UpVersionRules> getVersionRulesList(Map<String, Object> params);
	
}