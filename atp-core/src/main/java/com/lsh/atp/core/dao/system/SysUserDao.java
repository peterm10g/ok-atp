package com.lsh.atp.core.dao.system;



import com.lsh.atp.core.model.system.SysUser;
import com.lsh.atp.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SysUserDao {

	void insert(SysUser sysUser);
	
	void update(SysUser sysUser);

    SysUser getSysUserById(Integer id);
	
    SysUser getSysUser(Map<String, Object> params);

    Integer countSysUser(Map<String, Object> params);

    List<SysUser> getSysUserList(Map<String, Object> params);

}