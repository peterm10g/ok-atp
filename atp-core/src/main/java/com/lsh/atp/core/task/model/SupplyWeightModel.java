package com.lsh.atp.core.task.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by zhangqiang on 16/12/14.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SupplyWeightModel {

    private Long itemId;

    private String zoneCode;

    private String subZoneCode;
}
