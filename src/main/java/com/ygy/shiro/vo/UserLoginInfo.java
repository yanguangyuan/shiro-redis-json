package com.ygy.shiro.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
* project_name: shiro-redis-json
* package: com.ygy.shiro.vo
* describe: 登录提交信息
* @author : ygy
* creat_time: 2018年8月31日 上午9:56:49
* 
**/
@ApiModel
public class UserLoginInfo {

	/**
	 * 用户名
	 */
	@ApiModelProperty(name="username",value="用户名",required = true)
	private String username;
	/**
	 * 密码
	 */
	@ApiModelProperty(name="username",value="用户名",required = true)
	private String password;
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
