package com.lsh.atp.core.model.buffer;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by zhangqiang on 16/9/19.
 */
@XmlRootElement(name = "buffer")
public class SkuBufferConf implements Serializable{

    private Integer start;

    private Integer end;

    private Integer times;

    public SkuBufferConf() {
    }

    public SkuBufferConf(Integer start, Integer end, Integer times) {
        this.start = start;
        this.end = end;
        this.times = times;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
