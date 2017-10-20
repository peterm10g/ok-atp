package com.lsh.atp.core.dao.backup;

import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.backup.ItemDeliveryBackup;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ItemDeliveryBackupDao {

	int insert(ItemDeliveryBackup itemDeliveryBackup);


	int insertBatch(List<ItemDeliveryBackup> itemDeliveryBackupList);

	int deleteByTime(Long backupTime);

	int calBackUpDValue(Map<String,Object> params);

	String getUpdateTime(Map<String,Object> params);
	
}