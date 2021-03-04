package com.work.wb.util.model;

public class MechanicPriceModel {
	private int id;
	private int baseType;
	private String baseTypeName;
	private int basePriceType;
	private String basePriceTypeName;
	private int unitPrice;

	public MechanicPriceModel(int id,int baseType, int basePriceType, int unitPrice) {
		super();
		this.baseType = baseType;
		this.basePriceType = basePriceType;
		this.unitPrice = unitPrice;
		this.id = id;
	}
	
	public MechanicPriceModel(int id,int baseType, int basePriceType, int unitPrice,String baseTypeName,String basePriceTypeName) {
		super();
		this.baseType = baseType;
		this.basePriceType = basePriceType;
		this.unitPrice = unitPrice;
		this.id = id;
		this.basePriceTypeName = basePriceTypeName;
		this.baseTypeName = baseTypeName;
	}
    

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}



	public int getBaseType() {
		return baseType;
	}


	public void setBaseType(int baseType) {
		this.baseType = baseType;
	}


	public int getBasePriceType() {
		return basePriceType;
	}



	public void setBasePriceType(int basePriceType) {
		this.basePriceType = basePriceType;
	}



	public int getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}


	public String getBaseTypeName() {
		return baseTypeName;
	}


	public void setBaseTypeName(String baseTypeName) {
		this.baseTypeName = baseTypeName;
	}


	public String getBasePriceTypeName() {
		return basePriceTypeName;
	}


	public void setBasePriceTypeName(String basePriceTypeName) {
		this.basePriceTypeName = basePriceTypeName;
	}

}
