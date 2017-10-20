package com.lsh.atp.core.dao.system;


import com.lsh.atp.core.model.system.SysRole;
import com.lsh.atp.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SysRoleDao {

    void insert(SysRole sysRole);

    void update(SysRole sysRole);

    SysRole getSysRoleById(Integer id);

    Integer countSysRole(Map<String, Object> params);

    List<SysRole> getSysRoleList(Map<String, Object> params);

}