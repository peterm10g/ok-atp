package com.lsh.atp.core.dao.system;


import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.system.SysRoleFunctionRelation;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SysRoleFunctionRelationDao {

	void insert(SysRoleFunctionRelation sysRoleFunctionRelation);
	
	void update(SysRoleFunctionRelation sysRoleFunctionRelation);
	
	void deleteById(Integer id);
	
	SysRoleFunctionRelation getSysRoleFunctionRelationById(Integer id);

    Integer countSysRoleFunctionRelation(Map<String, Object> params);

    List<SysRoleFunctionRelation> getSysRoleFunctionRelationList(Map<String, Object> params);
	
}