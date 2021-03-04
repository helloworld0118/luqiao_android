package com.work.wb.util.model;

public class SkipMaterialPriceModel {

	private String materialType;
	private String basePriceType;
	private int unitPrice;

	public SkipMaterialPriceModel(String materialType, String basePriceType, int unitPrice) {
		super();
		this.materialType = materialType;
		this.basePriceType = basePriceType;
		this.unitPrice = unitPrice;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public String getBasePriceType() {
		return basePriceType;
	}

	public void setBasePriceType(String basePriceType) {
		this.basePriceType = basePriceType;
	}

	public int getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}

}
