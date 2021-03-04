package com.work.wb.util.model;

import java.io.Serializable;

public class ExpenseModel implements Serializable {
	private String person;
	private int price;
	private String date;
	private String createDate;
	private String priceType;
	private String billType;
	private String department;
	private String staffName;
	private String remark;
	public ExpenseModel(String person, int price, String date,String createDate, String priceType, String billType, String department,
			String staffName) {
		super();
		this.person = person;
		this.price = price;
		this.date = date;
		this.createDate = createDate;
		this.priceType = priceType;
		this.billType = billType;
		this.department = department;
		this.staffName = staffName;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
