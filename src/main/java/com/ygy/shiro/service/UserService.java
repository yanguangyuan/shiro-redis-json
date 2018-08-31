package com.ygy.shiro.service;

import com.ygy.shiro.po.User;

/**
* project_name: shiro-redis-json
* package: com.ygy.shiro.service
* describe: 用户service
* @author : ygy
* creat_time: 2018年8月30日 下午5:15:21
* 
**/
public interface UserService {
	
	/**
	 * 根据用户名或手机号查找
	 * @author ygy
	 * @param principal
	 * @return
	 * @throws Exception
	 * 2018年8月30日 下午5:35:49
	 */
	User selectByUsernameOrMobile(String principal) throws Exception;
}
