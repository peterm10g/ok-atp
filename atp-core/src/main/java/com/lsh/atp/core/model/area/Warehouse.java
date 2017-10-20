package com.lsh.atp.core.model.area;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Warehouse implements Serializable,Comparable<Warehouse> {

	private static final long serialVersionUID = 1397661330528956315L;

	private Long id;

	private Integer supplyId;

	private Integer supplyMarket;

	private String dcId;

	private String dcReal;

	private String name;

	private String zoneCode;

	private String subZoneCode;

	private Integer createdAt;

	private Integer updatedAt;

	private Integer weight;

	@Override
	public int compareTo(Warehouse other) {
		return 0 - this.supplyId.compareTo(other.getSupplyId());
	}

}
