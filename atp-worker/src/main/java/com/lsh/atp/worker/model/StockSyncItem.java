package com.lsh.atp.worker.model;

import com.lsh.atp.core.model.area.Warehouse;

/**
 * Created by huangdong on 16/7/20.
 */
public class StockSyncItem<T> {

    //private final AreaWarehouse warehouse;
    private final Warehouse warehouse;

    private final int timestamp;

    private final T data;

    public StockSyncItem(Warehouse warehouse, int timestamp, T data) {
        this.warehouse = warehouse;
        this.timestamp = timestamp;
        this.data = data;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public T getData() {
        return data;
    }
}
