package com.lsh.atp.api.model.baseVo;

import java.io.Serializable;
import java.util.Date;

/**
 * Project Name: lsh-atp
 * Created by peter on 16/7/6.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
public class BaseResponse implements Serializable{

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -8338882831901474666L;
    /**
     * 返回信息状态码
     */
    private Integer status;
    /**
     * 返回信息
     */
    private String meg;
    /**
     * 返回时间
     */
    private Date dataKey;

    public BaseResponse() {

    }

    public BaseResponse(Integer status, String meg, Date dataKey) {
        this.status = status;
        this.meg = meg;
        this.dataKey = dataKey;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMeg() {
        return meg;
    }

    public void setMeg(String meg) {
        this.meg = meg;
    }

    public Date getDataKey() {
        return (Date) dataKey.clone();
    }

    public void setDataKey(Date dataKey) {
        this.dataKey = dataKey;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status=" + status +
                ", meg='" + meg + '\'' +
                ", dataKey=" + dataKey +
                '}';
    }
}
