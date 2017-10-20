package com.lsh.atp.core.dao.inventory;


import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.inventory.InventoryLogic;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface InventoryLogicDao {

	int insert(InventoryLogic inventoryLogic);
	
	void update(InventoryLogic inventoryLogic);

    /**
     * 预占逻辑库存
     * @param inventoryLogic
     * @return
     */
    int preHoldLogicInventory(InventoryLogic inventoryLogic);

    /**
     * 同步逻辑库存
     *
     * @param dc 仓库编码
     * @param skuId 物料号
     * @return int
     */
    int syncInventoryQuantityByDc(@Param("dc") String dc, @Param("skuId") Long skuId);

    /**
     * 还原逻辑库存(预占后还原)
     *
     * @param id    id
     * @param qty   数量
     * @param skuHoldId  预占id
     *
     * @return  还原成功的数量
     * **/
    int restoreInventoryLogicByHold(@Param("id")Long id,@Param("qty") Long qty,@Param("skuHoldId") Long skuHoldId);

    /**
     * 还原逻辑库存(扣减后还原),直接还原库存数量
     *
     * @param id    id
     * @param qty   数量
     * @param skuHoldId 预占id
     *
     * @return  还原成功的数量
     * **/
    int restoreInventoryLogicByDecrease(@Param("id")Long id,@Param("qty") Long qty,@Param("skuHoldId") Long skuHoldId);

    /**
     * 还原逻辑库存(扣减后还原),还原预占数量
     *
     * @param skuHoldId 预占id
     * @param skuId     skuid
     * @param qty       数量
     * @param areaId    areaid
     *
     * @return  还原成功的数量
     * **/
    int restoreHoldByDecrease(@Param("skuId")Long skuId, @Param("areaId") String areaId, @Param("qty")Long qty,@Param("skuHoldId") Long skuHoldId);


    /**
     * 扣减预占库存
     * @param params 参数map
     *
     * @return int
     */
    Integer decreaseHoldSkuInventory(Map<String, Object> params);

    /**
     * 查询某区域商品库存数
     * @param params 参数map
     * @return
     */
    List<InventoryLogic> getInventoryLogicListByMap(Map<String, Object> params);

    /**
     * 根据zoneCode,skuHoldId查商品明细
     */
    List<InventoryLogic> getInventoryLogicDetail(@Param("zoneCode") String zoneCode,@Param("itemId") Long itemId);




    /* ---------------------------------------------------------------------------*/
    /* ---------------------------------------------------------------------------*/
    /* ----------------------------- 同步库存方法 -------------------------------------*/
    /* ---------------------------------------------------------------------------*/
    /* ---------------------------------------------------------------------------*/

    int insertAll(@Param("list")List<InventoryLogic> list);

    int updateAll(@Param("list")List<InventoryLogic> list);

    /**
     * 同步逻辑库存
     * @param inventoryLogic inventoryLogic对象
     * @return int
     */
    int updateInventoryLogic(InventoryLogic inventoryLogic);


    /* ---------------------------------------------------------------------------*/
    /* ---------------------------------------------------------------------------*/
    /* --------------------------------- 查询方法 -------------------------------------*/
    /* ---------------------------------------------------------------------------*/
    /* ---------------------------------------------------------------------------*/

    /**
     * 查询inventoryLogic
     * @param inventoryLogic
     * @return
     */
    InventoryLogic getInventoryLogicByModel(InventoryLogic inventoryLogic);

    /**
     * 根据areaid 和 skuid 获取InventoryLogic对象
     *
     * @param skuId 商品id
     * @param dc 仓库
     *
     * @return InventoryLogic集合
     * **/
    List<InventoryLogic> getInventoryLogicBySupplyDc(@Param("dc") String dc,@Param("supply")Integer supply,@Param("zoneCode")String zoneCode, @Param("skuId") Long skuId);

    List<InventoryLogic> getInventoryLogicBySku(@Param("skuId") Long skuId,@Param("zoneCode")String zoneCode);

    /**
     * 查询库存数量
     * @param itemId
     * @param zoneCode
     * @param supply
     * @param dc
     * @return
     */
    Long getQty(@Param("itemId")Long itemId, @Param("zoneCode")String zoneCode, @Param("supply")Integer supply,@Param("dc")String dc);

    /* ---------------------------------------------------------------------------*/
    /* ---------------------------------------------------------------------------*/
    /* --------------------------------- 预占扣减方法 ---------------------------------*/
    /* ---------------------------------------------------------------------------*/
    /* ---------------------------------------------------------------------------*/

    /**
     * 直接扣减逻辑库存
     * @param itemId
     * @param zoneCode
     * @param dc
     * @param holdNum
     * @return
     */
    int decreaseLogicInventory(@Param("itemId")Long itemId, @Param("zoneCode")String zoneCode,@Param("supply") Integer supply, @Param("dc")String dc, @Param("holdNum")Long holdNum);
}