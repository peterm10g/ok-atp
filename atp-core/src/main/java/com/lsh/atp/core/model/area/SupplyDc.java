package com.lsh.atp.core.model.area;


/**
 * Created by zhangqiang on 16/12/8.
 */
public class SupplyDc{

    private Integer supply;

    private String dc;

    public SupplyDc(Integer supply, String dc) {
        this.supply = supply;
        this.dc = dc;
    }

    public SupplyDc(Long supply, String dc){
        this(supply.intValue(),dc);
    }

    /**
     * 格式为 supply:dc 的字符串
     * @param supplyDc
     */
    public SupplyDc(String supplyDc){
        int index = supplyDc.indexOf(":");
        this.supply = Integer.parseInt(supplyDc.substring(0,index));
        this.dc = supplyDc.substring(index + 1);
    }

    public Integer getSupply() {
        return supply;
    }

    public String getDc() {
        return dc;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.supply).append(":").append(this.dc);
        return sb.toString();
    }

    public static void main(String[] args){
        SupplyDc supplyDc = new SupplyDc("1:DC40-1");
        System.out.println(supplyDc.getDc());
        System.out.println(supplyDc.getSupply());
    }
}