package com.work.wb.util.model;


public class MaterialModel {
	private int mid;
	private int sid;
	private int mtid;
	private String name;
	private String mobile;
	private String mtName;
	private int baseType;
	private String baseTypeName;
	private String createDate;
	private String mAddress;
	private int price;
	private String startDate;
	private String materialSpecName;
	private int materialSpec;
	public MaterialModel(int id,int sid,int mtid,String name, String mobile, String mtName, int baseType, String baseTypeName,
			String createDate,String mAddress,int price,String startDate,String materialSpecName, int materialSpec) {
		super();
		this.mid = id;
		this.sid = sid;
		this.mtid = mtid;
		this.name = name;
		this.mobile = mobile;
		this.mtName = mtName;
		this.baseType = baseType;
		this.baseTypeName = baseTypeName;
		this.createDate = createDate;
		this.mAddress = mAddress;
		this.price = price;
		this.startDate = startDate;
		this.materialSpecName = materialSpecName;
		this.materialSpec= materialSpec;
	}
	
	


	public int getMtid() {
		return mtid;
	}




	public void setMtid(int mtid) {
		this.mtid = mtid;
	}




	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMtName() {
		return mtName;
	}
	public void setMtName(String mtName) {
		this.mtName = mtName;
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
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getmAddress() {
		return mAddress;
	}
	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
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
}
