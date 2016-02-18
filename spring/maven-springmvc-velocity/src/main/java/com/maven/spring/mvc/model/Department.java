package com.maven.spring.mvc.model;

public class Department {

	private Integer deptId;
	private String deptNo;
	private String deptName;
	private String location;

	public Department() {
	}

	public Department(Integer deptId, String deptName, String location) {
		this.deptId = deptId;
		this.deptNo = "D" + deptId;
		this.deptName = deptName;
		this.location = location;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}