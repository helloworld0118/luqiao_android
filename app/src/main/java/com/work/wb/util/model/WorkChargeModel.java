package com.work.wb.util.model;


public class WorkChargeModel {

	private int id;

	private int workerType;
	private String workerTypeName;
	private int baseType;
	private String baseTypeName;
	private int count;
	private int basePriceType;
	private String basePriceTypeName;
	private int price;
	
	
	
	



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
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

	public int getWorkerType() {
		return workerType;
	}

	public void setWorkerType(int workerType) {
		this.workerType = workerType;
	}

	public String getWorkerTypeName() {
		return workerTypeName;
	}

	public void setWorkerTypeName(String workerTypeName) {
		this.workerTypeName = workerTypeName;
	}
}
