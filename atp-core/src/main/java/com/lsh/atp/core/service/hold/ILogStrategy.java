package com.lsh.atp.core.service.hold;

import com.lsh.atp.api.model.baseVo.Item;

import java.util.List;

/**
 * lsh-atp
 * Created by peter on 16/7/19.
 */
public interface ILogStrategy {

    void insertLog(int channel, String sequence, int operationType, Long holdId, List<Item> items);
}
