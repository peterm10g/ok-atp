package com.lsh.atp.api.model.mis;

import com.lsh.atp.api.model.baseVo.BaseResponse;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by zhangqiang on 17/7/28.
 */
public class CurrentDcGetResponse extends BaseResponse {

    @JsonProperty("current_dc")
    private String currentDc;

    public String getCurrentDc() {
        return currentDc;
    }

    public void setCurrentDc(String currentDc) {
        this.currentDc = currentDc;
    }
}
