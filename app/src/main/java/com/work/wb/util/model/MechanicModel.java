package com.work.wb.util.model;

public class MechanicModel {
	private String plateNumber;
	
	private int  id;
	private int  foremanId;
	
	private String name;
	private String mName;
	private String mobile;
	private String idcard;
	private String driverName;
	private String driverMobile;
	private String driverIdcard;
	private String createDate;
	private String model;
	private String capacity;
	private int baseType;
	private String baseTypeName;
	private int basePriceType;
	private String basePriceTypeName;
	private int price;

	public MechanicModel(int id, int foremanId,String plateNumber, String name, String mobile, String idcard, String driverName,
			String driverMobile, String driverIdcard, String createDate) {
		super();
		this.id = id;
		this.foremanId = foremanId;
		this.plateNumber = plateNumber;
		this.name = name;
		this.mobile = mobile;
		this.idcard = idcard;
		this.driverName = driverName;
		this.driverMobile = driverMobile;
		this.driverIdcard = driverIdcard;
		this.createDate = createDate;
	}
	
	public MechanicModel(int id, int foremanId,String plateNumber, String name, String mobile, String idcard, String driverName,
			String driverMobile, String driverIdcard, String createDate,String capacity) {
		super();
		this.id = id;
		this.foremanId = foremanId;
		this.plateNumber = plateNumber;
		this.name = name;
		this.mobile = mobile;
		this.idcard = idcard;
		this.driverName = driverName;
		this.driverMobile = driverMobile;
		this.driverIdcard = driverIdcard;
		this.createDate = createDate;
		this.capacity = capacity;
	}
	
	
	public MechanicModel(int id, int foremanId,String plateNumber, String name, String mobile, String idcard, String driverName,
			String driverMobile, String driverIdcard, String createDate,String capacity,String baseTypeName,String basePriceTypeName,int price) {
		super();
		this.id = id;
		this.foremanId = foremanId;
		this.plateNumber = plateNumber;
		this.name = name;
		this.mobile = mobile;
		this.idcard = idcard;
		this.driverName = driverName;
		this.driverMobile = driverMobile;
		this.driverIdcard = driverIdcard;
		this.createDate = createDate;
		this.capacity = capacity;
		this.baseTypeName = baseTypeName;
		this.basePriceTypeName = basePriceTypeName;
		this.price = price;
	}

	public MechanicModel(int id, int foremanId,String plateNumber, String name, String mName, String mobile, String idcard, String driverName,
			String driverMobile, String driverIdcard, String createDate, String model, int basePriceType,String basePriceTypeName,
			int price) {
		super();
		this.id = id;
		this.foremanId = foremanId;
		this.plateNumber = plateNumber;
		this.name = name;
		this.mName = mName;
		this.mobile = mobile;
		this.idcard = idcard;
		this.driverName = driverName;
		this.driverMobile = driverMobile;
		this.driverIdcard = driverIdcard;
		this.createDate = createDate;
		this.model = model;
		this.basePriceType = basePriceType;
		this.basePriceTypeName = basePriceTypeName;
		this.price = price;
	}
	
	public MechanicModel(int id, int foremanId,String plateNumber, String name, String mName, String mobile, String idcard, String driverName,
			String driverMobile, String driverIdcard, String createDate, String model) {
		super();
		this.id = id;
		this.foremanId = foremanId;
		this.plateNumber = plateNumber;
		this.name = name;
		this.mName = mName;
		this.mobile = mobile;
		this.idcard = idcard;
		this.driverName = driverName;
		this.driverMobile = driverMobile;
		this.driverIdcard = driverIdcard;
		this.createDate = createDate;
		this.model = model;
	}
	
	
	public MechanicModel(int id, int foremanId,String plateNumber, String name, String mName, String mobile, String idcard, String driverName,
			String driverMobile, String driverIdcard, String createDate, String model,int baseType,String baseTypeName, int basePriceType,String basePriceTypeName,
			int price) {
		super();
		this.id = id;
		this.foremanId = foremanId;
		this.plateNumber = plateNumber;
		this.name = name;
		this.mName = mName;
		this.mobile = mobile;
		this.idcard = idcard;
		this.driverName = driverName;
		this.driverMobile = driverMobile;
		this.driverIdcard = driverIdcard;
		this.createDate = createDate;
		this.model = model;
		this.baseType = baseType;
		this.baseTypeName = basePriceTypeName;
		this.basePriceType = basePriceType;
		this.basePriceTypeName = basePriceTypeName;
		this.price = price;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getBasePriceTypeName() {
		return basePriceTypeName;
	}

	public void setBasePriceTypeName(String basePriceTypeName) {
		this.basePriceTypeName = basePriceTypeName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getForemanId() {
		return foremanId;
	}

	public void setForemanId(int foremanId) {
		this.foremanId = foremanId;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverMobile() {
		return driverMobile;
	}

	public void setDriverMobile(String driverMobile) {
		this.driverMobile = driverMobile;
	}

	public String getDriverIdcard() {
		return driverIdcard;
	}

	public void setDriverIdcard(String driverIdcard) {
		this.driverIdcard = driverIdcard;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getBasePriceType() {
		return basePriceType;
	}

	public void setBasePriceType(int basePriceType) {
		this.basePriceType = basePriceType;
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

	public String getBaseTypeName() {
		return baseTypeName;
	}

	public void setBaseTypeName(String baseTypeName) {
		this.baseTypeName = baseTypeName;
	}


}
