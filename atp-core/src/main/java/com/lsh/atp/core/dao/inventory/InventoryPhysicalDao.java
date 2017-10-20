package com.lsh.atp.core.dao.inventory;

import com.lsh.atp.core.dao.BaseDao;
import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.inventory.InventoryPhysical;

/**
 * Created by huangdong on 16/7/7.
 */
@MyBatisRepository
public interface InventoryPhysicalDao extends BaseDao<InventoryPhysical, Long> {
}
