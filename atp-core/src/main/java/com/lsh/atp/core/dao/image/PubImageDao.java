package com.lsh.atp.core.dao.image;



import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.image.PubImage;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PubImageDao {

	void insert(PubImage PubImage);
	
	void update(PubImage PubImage);
	
	PubImage getPubImageById(Integer id);

    Integer countPubImage(Map<String, Object> params);

    List<PubImage> getPubImageList(Map<String, Object> params);
	
}