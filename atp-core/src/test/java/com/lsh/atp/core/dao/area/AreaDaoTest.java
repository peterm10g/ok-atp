package com.lsh.atp.core.dao.area;

import com.lsh.atp.core.common.BaseSpringTest;
import com.lsh.atp.core.dao.area.AreaDao;
import com.lsh.atp.core.model.area.Area;
import com.lsh.atp.core.model.system.SysUser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huangdong on 16/7/7.
 */
public class AreaDaoTest extends BaseSpringTest {

    @Autowired
    private AreaDao dao;

    @Test
    public void test() throws Exception {
        Area area = dao.get(1);
        System.out.println(area);
    }
}
