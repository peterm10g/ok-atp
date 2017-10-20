package com.lsh.atp.core.dao.up;


import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.up.UpVersionRulesCons;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpVersionRulesConsDao {

	void insert(UpVersionRulesCons versionRulesCons);
	
	void update(UpVersionRulesCons versionRulesCons);

	void delete(Long id);
	
	UpVersionRulesCons getVersionRulesConsById(Long id);

    Integer countVersionRulesCons(Map<String, Object> params);

    List<UpVersionRulesCons> getVersionRulesConsList(Map<String, Object> params);
	
}