package com.lsh.atp.core.model.backup;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemDeliveryBackup implements Serializable {

	/**  */
    private Long id;
	/** 商品id */
    private String skuId;
	/** 采购商id */
    private Integer marketId;
	/**  */
    private String kunnr;
	/**  */
    private String werks;
	/**  */
    private String maktx;
	/**  */
    private String mmstatxt;
	/**  */
    private String zkhspbm;
	/**  */
    private BigDecimal lbkum;
	/**  */
    private String meins;
	/**  */
    private String zdate;
	/**  */
    private String ztime;
	/**  */
    private String zuname;
	/** 备份时间 */
    private Long backuptime;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(String skuId){
		this.skuId = skuId;
	}
	
	public Integer getMarketId(){
		return this.marketId;
	}
	
	public void setMarketId(Integer marketId){
		this.marketId = marketId;
	}
	
	public String getKunnr(){
		return this.kunnr;
	}
	
	public void setKunnr(String kunnr){
		this.kunnr = kunnr;
	}
	
	public String getWerks(){
		return this.werks;
	}
	
	public void setWerks(String werks){
		this.werks = werks;
	}
	
	public String getMaktx(){
		return this.maktx;
	}
	
	public void setMaktx(String maktx){
		this.maktx = maktx;
	}
	
	public String getMmstatxt(){
		return this.mmstatxt;
	}
	
	public void setMmstatxt(String mmstatxt){
		this.mmstatxt = mmstatxt;
	}
	
	public String getZkhspbm(){
		return this.zkhspbm;
	}
	
	public void setZkhspbm(String zkhspbm){
		this.zkhspbm = zkhspbm;
	}
	
	public BigDecimal getLbkum(){
		return this.lbkum;
	}
	
	public void setLbkum(BigDecimal lbkum){
		this.lbkum = lbkum;
	}
	
	public String getMeins(){
		return this.meins;
	}
	
	public void setMeins(String meins){
		this.meins = meins;
	}
	
	public String getZdate(){
		return this.zdate;
	}
	
	public void setZdate(String zdate){
		this.zdate = zdate;
	}
	
	public String getZtime(){
		return this.ztime;
	}
	
	public void setZtime(String ztime){
		this.ztime = ztime;
	}
	
	public String getZuname(){
		return this.zuname;
	}
	
	public void setZuname(String zuname){
		this.zuname = zuname;
	}
	
	public Long getBackuptime(){
		return this.backuptime;
	}
	
	public void setBackuptime(Long backuptime){
		this.backuptime = backuptime;
	}
	
	
}
