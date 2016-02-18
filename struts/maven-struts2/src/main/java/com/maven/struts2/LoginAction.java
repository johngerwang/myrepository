package com.maven.struts2;

import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {

    private static final long serialVersionUID = 7299264265184515893L;
    private String username;
    private String password;

    @Override
    public String execute() {
        if (this.username != null && this.password != null
                && this.username.equals("admin")
                && this.password.equals("admin123")) {
            return "success";
        } else {
            addActionError(getText("error.login"));//从ApplicationResources.properties文件中获得error.login定义的字符串
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