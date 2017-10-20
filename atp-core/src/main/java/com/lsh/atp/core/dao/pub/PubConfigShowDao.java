package com.lsh.atp.core.dao.pub;


import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.pub.PubConfigShow;

@MyBatisRepository
public interface PubConfigShowDao {

	void insert(PubConfigShow pubConfigShow);
	
	void update(PubConfigShow pubConfigShow);
	
	PubConfigShow getPubConfigShowById(Integer id);

	PubConfigShow getPubConfigShowByCode(String code);

}