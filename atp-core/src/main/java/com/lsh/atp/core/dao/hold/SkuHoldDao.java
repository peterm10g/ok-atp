package com.lsh.atp.core.dao.hold;

import com.lsh.atp.core.dao.MyBatisRepository;
import com.lsh.atp.core.model.hold.SkuHold;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SkuHoldDao {
    /**
     * 新增一条预占商品(SkuHold)
     *
     * @param skuHold skuHold对象
     **/
    Integer insert(SkuHold skuHold);

    /**
     * 更新预占商品(SkuHold)
     *
     * @param skuHold skuHold对象
     **/
    Integer update(SkuHold skuHold);

    /**
     * 根据id查询一条SkuHold记录
     *
     * @param id id
     * @return SkuHold对象
     **/
    SkuHold getSkuHoldById(Long id);

    /**
     * 根据holdNo查询一条SKuHold纪录
     *
     * @param holdNo holdNo
     * @return SkuHold对象
     * **/
    SkuHold getSkuHoldByHoldNo(@Param("holdNo") String holdNo);

    /**
     * 根据条件map查询符合条件的数据总条数
     *
     * @param params map
     *
     * @return 符合条件的数量
     **/
    Integer countSkuHold(Map<String, Object> params);

    /**
     * 根据条件map查询符合的SkuHold
     *
     * @param params map
     *
     * @return SkuHold对象集合
     * **/
    List<SkuHold> getSkuHoldList(Map<String, Object> params);

    /**
     * 根据id和流水号查询
     *
     * @param id         预占id
     * @param sequenceId 流水号
     * @return SkuHold对象
     **/
    SkuHold getSkuHoldByIdAndSequenceId(@Param("id") Long id, @Param("sequenceId") String sequenceId);

    /**
     * 更新预占库存记录的状态(status) 目前是扣减更新为2,还原更新为3
     *
     * @param skuHold SkuHold对象
     *
     * @return 返回更新成功的条数
     * **/
    Integer updateStatus(SkuHold skuHold);

    /**
     * 根据条件map查询符合的SkuHold 带有 SkuholdQty
     *
     * @param params 参数
     *
     * @return SkuHold对象集合
     * **/
    List<SkuHold> getSkuHoldDetailList(Map<String, Object> params);


    /**
     * 根据渠道和流水号查询
     *
     * @param channel    渠道
     * @param sequence   流水号
     * @return SkuHold对象
     **/
    SkuHold getSkuHoldByChannelAndSequence(@Param("channel") int channel, @Param("sequence") String sequence);

    /**
     * 获取预占ID
     *
     * @param sequences
     * @return
     */
    List<Long> getSkuHoldIdBySequenceId(@Param("sequences") Collection<String> sequences);
}