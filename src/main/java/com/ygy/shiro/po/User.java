package com.ygy.shiro.po;

import java.io.Serializable;
public class User implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3544512946277165287L;
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
