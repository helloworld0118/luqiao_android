package com.work.wb.util.model;

import com.work.wb.entity.Project;
import com.work.wb.entity.Role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ProjectRoleModel implements Serializable {
	private Project project;
	private List<Role> roles = new ArrayList<>();

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
