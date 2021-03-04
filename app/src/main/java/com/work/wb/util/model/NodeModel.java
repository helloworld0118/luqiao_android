package com.work.wb.util.model;

public class NodeModel {
	private String node;
	private String program;
	private String date;


	public NodeModel(String node, String program,String date) {
		super();
		this.node = node;
		this.program = program;
		this.date= date;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}


	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}



}