package com.ygy.shiro.controller;

import java.util.HashMap;
import java.util.Map;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ygy.shiro.vo.UserLoginInfo;

/**
* project_name: shiro-redis-json
* package: com.ygy.shiro.controller
* describe: TODO
* @author : ygy
* creat_time: 2018年8月31日 上午9:23:33
* 
**/
@Controller
public class UserController {

	protected final static Logger logger = LogManager.getLogger(UserController.class);
	
	/**
	 * @author ygy
	 * @param username
	 * @param password
	 * @return
	 * 2018年8月31日 上午9:30:01
	 */
	@ResponseBody
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public Map<String,Object> login(@RequestBody UserLoginInfo info){
		Map<String,Object> resultMap = new HashMap<>();
		String username= info.getUsername();
		String password = info.getPassword();
		UsernamePasswordToken token  = new UsernamePasswordToken(username, password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
		}catch (AccountException  e) {
			logger.info("用户名："+username+"账号或密码错误");
			resultMap.put("status", 50001);
            resultMap.put("message", "帐号或密码错误！");
		}catch (Exception e) {
			logger.info(e.getMessage());
			resultMap.put("status", 50001);
            resultMap.put("message", "登录失败");
		}
		Session session = subject.getSession();
		System.out.println(session.getId());
		return resultMap;
	}
}
