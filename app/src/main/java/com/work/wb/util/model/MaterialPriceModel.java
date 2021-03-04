package com.work.wb.util.model;


public class MaterialPriceModel {
	private int baseType;
	private String baseTypeName;
	private String materialSpecName;
	private int materialSpec;
	private int price;
	
	
	
	public MaterialPriceModel(int baseType, String baseTypeName, String materialSpecName, int materialSpec, int price) {
		super();
		this.baseType = baseType;
		this.baseTypeName = baseTypeName;
		this.materialSpecName = materialSpecName;
		this.materialSpec = materialSpec;
		this.price = price;
	}
	public int getBaseType() {
		return baseType;
	}
	public void setBaseType(int baseType) {
		this.baseType = baseType;
	}
	public String getBaseTypeName() {
		return baseTypeName;
	}
	public void setBaseTypeName(String baseTypeName) {
		this.baseTypeName = baseTypeName;
	}
	public String getMaterialSpecName() {
		return materialSpecName;
	}
	public void setMaterialSpecName(String materialSpecName) {
		this.materialSpecName = materialSpecName;
	}
	public int getMaterialSpec() {
		return materialSpec;
	}
	public void setMaterialSpec(int materialSpec) {
		this.materialSpec = materialSpec;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	
	
}

