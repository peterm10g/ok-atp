package com.lsh.atp.core.dao.pub;


import com.lsh.atp.core.model.pub.PubConfigDataFile;
import com.lsh.atp.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PubConfigDataFileDao {

	void insert(PubConfigDataFile pubConfigDataFile);
	
	void update(PubConfigDataFile pubConfigDataFile);

	void delete(Integer id);

	void deleteByDataId(Integer dataId);
	
	PubConfigDataFile getPubConfigDataFileById(Integer id);

    Integer countPubConfigDataFile(Map<String, Object> params);

    List<PubConfigDataFile> getPubConfigDataFileList(Map<String, Object> params);
	
}