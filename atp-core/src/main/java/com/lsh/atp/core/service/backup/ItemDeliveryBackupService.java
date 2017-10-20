package com.lsh.atp.core.service.backup;

import com.lsh.atp.core.dao.backup.ItemDeliveryBackupDao;
import com.lsh.atp.core.model.backup.ItemDeliveryBackup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Project Name: lsh-atp
 * Created by miaozhuang
 * Date: 16/7/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.core.service.hold.
 * desc:预占操作日志
 */
@Component
@Transactional(readOnly = true)
public class ItemDeliveryBackupService {

    private static Logger logger = LoggerFactory.getLogger(ItemDeliveryBackupService.class);

    @Autowired
    private ItemDeliveryBackupDao itemDeliveryBackupDao;

    /**
     * 插入预占日志
     *
     * @param ItemList
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public void insertLog(List<ItemDeliveryBackup> ItemList) {
        try {

            itemDeliveryBackupDao.insertBatch( ItemList);

        } catch (Exception e) {
            logger.error("记录日志异常",e);
        }

    }


}
