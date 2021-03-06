package com.lsh.atp.core.dao.system;



import com.lsh.atp.core.model.system.SysUserRoleRelation;
import com.lsh.atp.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SysUserRoleRelationDao {

	void insert(SysUserRoleRelation sysUserRoleRelation);
	
	void update(SysUserRoleRelation sysUserRoleRelation);

    void deleteByUserId(Integer userId);
	
	SysUserRoleRelation getSysUserRoleRelationById(Integer id);

    Integer countSysUserRoleRelation(Map<String, Object> params);

    List<SysUserRoleRelation> getSysUserRoleRelationList(Map<String, Object> params);

}