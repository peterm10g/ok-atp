package com.lsh.atp.api.service.mis;

import com.lsh.atp.api.model.mis.CurrentDcGetRequest;
import com.lsh.atp.api.model.mis.CurrentDcGetResponse;

/**
 * Created by zhangqiang on 17/7/28.
 */
public interface ICurrentDcRestService {

    CurrentDcGetResponse getCurrentDc(CurrentDcGetRequest request);

}
