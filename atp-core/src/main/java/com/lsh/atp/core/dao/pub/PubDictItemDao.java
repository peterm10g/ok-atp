package com.lsh.atp.core.dao.pub;

import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.pub.PubDictItem;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PubDictItemDao {

	void insert(PubDictItem pubDictItem);
	
	void update(PubDictItem pubDictItem);
	
	PubDictItem getPubDictItemById(Integer id);

    Integer countPubDictItem(Map<String, Object> params);

    List<PubDictItem> getPubDictItemList(Map<String, Object> params);
	
}