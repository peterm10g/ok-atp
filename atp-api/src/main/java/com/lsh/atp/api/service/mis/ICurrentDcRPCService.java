package com.lsh.atp.api.service.mis;

import com.lsh.atp.api.model.mis.CurrentDcGetRequest;
import com.lsh.atp.api.model.mis.CurrentDcGetResponse;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang
 * Date: 16/10/17
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.api.service.mis
 * desc:有关当前售卖仓库RPC接口
 */
public interface ICurrentDcRPCService {

    CurrentDcGetResponse getCurrentDc(CurrentDcGetRequest request);

}
