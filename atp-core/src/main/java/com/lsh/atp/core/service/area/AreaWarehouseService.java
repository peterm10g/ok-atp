package com.lsh.atp.core.service.area;

import com.lsh.atp.core.dao.area.AreaWarehouseDao;
import com.lsh.atp.core.model.area.AreaWarehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huangdong on 16/7/18.
 */
@Service
public class AreaWarehouseService {

    @Autowired
    private AreaWarehouseDao areaWarehouseDao;

    /**
     * 根据仓库编码获取区域仓库信息
     *
     * @param warehouseCode
     * @return
     */
    public AreaWarehouse getAreaWarehouse(String warehouseCode) {
        AreaWarehouse param = new AreaWarehouse();
        param.setWarehouseCode(warehouseCode);
        return areaWarehouseDao.findOne(param);
    }
}
