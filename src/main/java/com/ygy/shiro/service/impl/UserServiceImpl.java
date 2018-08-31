package com.ygy.shiro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ygy.shiro.mapper.UserMapper;
import com.ygy.shiro.po.User;
import com.ygy.shiro.service.UserService;
import com.ygy.shiro.utils.StringUtils;
/**
* project_name: shiro-redis-json
* package: com.ygy.shiro.service.impl
* describe: TODO
* @author : ygy
* creat_time: 2018年8月30日 下午5:44:13
* 
**/
@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Override
	public User selectByUsernameOrMobile(String principal) throws Exception {
		if(StringUtils.isEmpty(principal)) {
			throw new Exception("主要信息不能为空");
		}
		return userMapper.selectByUsernameOrMobile(principal);
	}

}
