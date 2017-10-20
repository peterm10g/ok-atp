package com.lsh.atp.core.dao.es;

import com.lsh.atp.core.task.model.InventoryRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by zhangqiang on 17/5/11.
 */
public interface InventoryRecordEsDao extends ElasticsearchRepository<InventoryRecord, String>{

}