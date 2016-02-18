package com.maven.struts2;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

//@Results({
//	@Result(name = "success", location = "/WEB-INF/pages/UserInfo.jsp"),
//	@Result(name = "error", location = "/WEB-INF/pages/Login.jsp") })
public class LogonAction extends ActionSupport {

	private static final long serialVersionUID = 7299264265184515893L;
	private String username;
	private String password;

	public LogonAction() {

	}

	@Action(value = "logon", results = {
	           @Result(name = "success", location = "/WEB-INF/pages/UserInfo.jsp"),
	           @Result(name = "error", location = "/WEB-INF/pages/Logon.jsp") })
	@Override
	public String execute() {
		if ("admin".equals(this.username) && "admin123".equals(this.password)) {
			return "success";
		} else {
			addActionError(getText("error.login"));
			return "error";
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}