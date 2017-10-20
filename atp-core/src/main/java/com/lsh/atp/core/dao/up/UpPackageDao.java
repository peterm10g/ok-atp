package com.lsh.atp.core.dao.up;

import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.up.UpPackage;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpPackageDao {

	void insert(UpPackage upPackage);
	
	void update(UpPackage upPackage);

	UpPackage getPackageById(Long id);

    Integer countPackage(Map<String, Object> params);

    List<UpPackage> getPackageList(Map<String, Object> params);
	
}