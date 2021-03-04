package com.work.wb.util.model;

import android.util.Log;

import com.work.wb.entity.Staff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class UserModel implements Serializable {
	private Staff staff;
	private String token;
	private List<ProjectRoleModel> projectRoles = new ArrayList<>();

	public Staff getStaff() {
		Log.e("UserModel",staff.getId()+"=------------UserModel");
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public List<ProjectRoleModel> getProjectRoles() {
		return projectRoles;
	}

	public void setProjectRoles(List<ProjectRoleModel> projectRoles) {
		this.projectRoles = projectRoles;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
