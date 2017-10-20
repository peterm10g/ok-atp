package com.lsh.atp.core.dao.pub;

import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.pub.PubDict;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PubDictDao {

	void insert(PubDict pubDict);
	
	void update(PubDict pubDict);
	
	PubDict getPubDictById(Integer id);

    Integer countPubDict(Map<String, Object> params);

    List<PubDict> getPubDictList(Map<String, Object> params);
	
}