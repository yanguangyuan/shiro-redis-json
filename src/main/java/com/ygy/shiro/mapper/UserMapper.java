package com.ygy.shiro.mapper;

import org.springframework.stereotype.Repository;

import com.ygy.shiro.po.User;

/**
* project_name: shiro-redis-json
* package: com.ygy.shiro.mapper
* describe: 用户表mapper
* @author : 严光远
* creat_time: 2018年8月30日 下午5:16:57
* 
**/
@Repository("userMapper")
public interface UserMapper {
	/**
	 * 根据用户名或手机查找
	 * @author ygy
	 * @param principal
	 * @return
	 * @throws Exception
	 * 2018年8月30日 下午5:32:03
	 */
	User selectByUsernameOrMobile(String principal) throws Exception;
}
