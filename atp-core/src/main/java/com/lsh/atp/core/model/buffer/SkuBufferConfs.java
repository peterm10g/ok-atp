package com.lsh.atp.core.model.buffer;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangqiang on 16/9/19.
 */
public class SkuBufferConfs implements Serializable{

    private List<SkuBufferConf> buffers;

    public SkuBufferConfs() {
    }

    public SkuBufferConfs(List<SkuBufferConf> buffers) {
        this.buffers = buffers;
    }

    public List<SkuBufferConf> getBuffers() {
        return buffers;
    }

    public void setBuffers(List<SkuBufferConf> buffers) {
        this.buffers = buffers;
    }
}
