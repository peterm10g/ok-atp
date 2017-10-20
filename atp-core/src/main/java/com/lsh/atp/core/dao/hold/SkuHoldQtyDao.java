package com.lsh.atp.core.dao.hold;

import com.lsh.atp.api.model.baseVo.Item;
import com.lsh.atp.api.model.baseVo.ItemDc;
import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.hold.SkuHoldQty;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SkuHoldQtyDao {
    /**
     * 新增一条预占商品明细(SkuHoldQty)
     *
     * @param skuHoldQty    skuHoldQty对象
     * @return 插入成功的数量
     **/
    Integer insert(SkuHoldQty skuHoldQty);

    Integer insertAll(@Param("list") List<SkuHoldQty> skuHoldQtyList);

    /**
     * 更新(SkuHoldQty)
     *
     * @param skuHoldQty    skuHoldQty对象
     **/
    int update(SkuHoldQty skuHoldQty);

    /**
     * 根据id查询一条SkuHoldQty记录
     *
     * @param id    id
     * @return SkuHoldQty对象
     **/
    SkuHoldQty getSkuHoldQtyById(Integer id);

    /**
     * 根据条件map查询符合条件的数据总条数
     *
     * @param params    map
     * @return 符合条件的数量
     **/
    Integer countSkuHoldQty(Map<String, Object> params);

    /**
     * 根据条件map查询符合的SkuHoldQty集合
     *
     * @param params    map
     * @return SkuHoldQty对象集合
     **/
    List<SkuHoldQty> getSkuHoldQtyList(Map<String, Object> params);

    /**
     * 根据id查询预占明细记录 并更新数量
     *
     * @param id    id
     * @param qty   数量
     * @return 回更新成功的数量
     **/
    Integer preHoldQty(@Param("id") Long id, @Param("qty") Long qty);

    /**
     * 根据holdId和skuId查询预占明细记录,并更新数量
     *
     * @param holdId    预占id
     * @param itemId    商品id
     * @param qty       数量
     * @return 回更新成功的数量
     **/
    Integer preHoldHoldQty(@Param("holdId") String holdId, @Param("itemId") Long itemId, @Param("qty") Long qty);

    /**
     * 查询预占id(holdid)对应的所有明细记录集合
     *
     * @param holdId    预展id
     * @return 预占id(holdid)对应的所有明细记录集合
     **/
    List<SkuHoldQty> getSkuHoldQtyByHoldId(@Param("holdId") Long holdId);

    /**
     * 根据holdid 和 skuid 查询 预展明细记录-SkuHoldQty对象
     *
     * @param holdId    预占id
     * @param itemId    商品id
     * @return SkuHoldQty对象
     **/
    SkuHoldQty getSkuHoldByHoldIdAndSkuId(@Param("holdId") String holdId, @Param("itemId") Long itemId);

    /**
     * 根据holdid 和 Item类的集合 获取SkuHoldQty集合
     *
     * @param holdId    预占id
     * @param items Item集合
     * @return SkuHoldQty集合
     **/
    List<SkuHoldQty> getSkuHoldListByHoldIdAndSkuIdList(@Param("holdId") String holdId,@Param("areaId")String areaId, @Param("skuList") List<Item> items);


    /**
     * 根据HoldId和SkuId 获取主键Id
     *
     * @param holdId 预展id
     * @param itemId    商品id
     * @return 主 id
     **/
    Long getIdByHoldIdAndSkuId(@Param("holdId") Long holdId, @Param("skuId") Long itemId);

    /**
     * (秒杀)还原库存
     *
     * @param id    id
     * @param holdQty 数量
     * @param sonSkuHoldId 子预占id,为了判断状态
     *
     * @return 原成功的数量
     **/
    int restoreSkuHoldQty(@Param("id") Long id, @Param("holdQty") Long holdQty , @Param("sonSkuHoldId")Long sonSkuHoldId);

    /**
     * 根据holdid 和 Item类的集合 获取SkuHoldQty集合(for 寄售查询)
     *
     * @param holdId    预占id
     * @param items  Item集合
     * @return SkuHoldQty集合
     **/
    List<SkuHoldQty> getSkuHoldQtyForConsignment(@Param("holdId") String holdId,@Param("skuList") List<ItemDc> items);

    /**
     * 增加还原库存数量
     *
     * @param dc
     * @param holdId
     * @param num
     * @param skuId
     *
     * @return
     * **/
    int addRestoreQty(@Param("holdId")Long holdId,@Param("skuId")Long skuId,@Param("dc")String dc, @Param("num")Long num);

    /**
     * 查询所有有该商品的订单号的总量
     *
     * @param sequences
     * @param items
     * @param dc
     * @return 总量
     */
    List<SkuHoldQty> getTotalHoldQty(@Param("sequences") Collection<String> sequences, @Param("items") Collection<Long> items, @Param("dc") String dc);

    /**
     * 查询所有有该商品的订单号的总量
     *
     * @param holdIds
     * @param dc
     * @return 总量
     */
    List<SkuHoldQty> getTotalHoldQtyBySkuIdAndDc(@Param("holdIds")Collection<Long> holdIds, @Param("dc") String dc);
    /**
     * 查询所有有该商品的订单号的总量
     *
     * @param sequences
     * @param itemId
     * @param dc
     * @return 总量
     */
    Integer getTotalHoldQtySingle(@Param("sequences") Collection<String> sequences, @Param("itemId") Long itemId,  @Param("dc") String dc);

}