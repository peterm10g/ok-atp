package com.lsh.atp.api.service.hold;

import com.lsh.atp.api.model.Hold.HoldDetailQueryRequest;
import com.lsh.atp.api.model.Hold.HoldDetailQueryResponse;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang on 16/8/23.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
public interface IHoldDatailQueryRPCService {

    HoldDetailQueryResponse queryHoldDetail(HoldDetailQueryRequest request);
}
