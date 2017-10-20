package com.lsh.atp.core.dao.up;


import com.lsh.atp.core.model.up.UpChannel;
import com.lsh.atp.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpChannelDao {

	void insert(UpChannel channel);
	
	void update(UpChannel channel);
	
	UpChannel getChannelById(Integer id);

    Integer countChannel(Map<String, Object> params);

    List<UpChannel> getChannelList(Map<String, Object> params);
	
}