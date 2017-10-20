package com.lsh.atp.api.model.mis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created by zhangqiang on 17/9/4.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class WarehouseDto {

    private String dc;

    @JsonProperty("sale_area_code")
    private String subZoneCode;
}
