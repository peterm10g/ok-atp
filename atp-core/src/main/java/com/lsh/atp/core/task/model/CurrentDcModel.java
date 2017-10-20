package com.lsh.atp.core.task.model;

import com.lsh.atp.core.model.area.SupplyDc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Created by zhangqiang on 16/12/14.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CurrentDcModel {

    private String sequence;

    private String zoneCode;

    private String subZoneCode;

    private Map<Long,SupplyDc> map;

}
