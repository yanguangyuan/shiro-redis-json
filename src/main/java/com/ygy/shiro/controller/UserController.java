package com.ygy.shiro.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ygy.shiro.common.Constant;
import com.ygy.shiro.common.ResponseEntity;
import com.ygy.shiro.po.User;
import com.ygy.shiro.service.UserService;
import com.ygy.shiro.vo.UserLoginInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* project_name: shiro-redis-json
* package: com.ygy.shiro.controller
* describe: TODO
* @author : ygy
* creat_time: 2018年8月31日 上午9:23:33
* 
**/
@Api(value = "用户控制器",description = "用于用户登录等控制控制器")
@Controller
public class UserController {

	protected final static Logger LOGGER = LogManager.getLogger(UserController.class);
	@Autowired
	private UserService UserService;
	/**
	 * @author ygy
	 * @param username
	 * @param password
	 * @return
	 * 2018年8月31日 上午9:30:01
	 */
	@ResponseBody
	@ApiOperation(value="登录")
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ResponseEntity<String> login(@RequestBody UserLoginInfo info){
		ResponseEntity<String> response = new ResponseEntity<>();
		//生成token
		String result = UUID.randomUUID().toString();
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		User user=null;
		try {
			user = UserService.selectByUsernameOrMobile(info.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info(e.getMessage());
		}
		session.setAttribute("userInfo", user);
		System.out.println(session.getId());
		response.setResult(result);
		response.setCode(Constant.REQUEST_SUCCESS_CODE);
		response.setRemark(Constant.REQUEST_SUCCESS_DESC);
		return response;
	}
	@ResponseBody
	@ApiOperation(value="获取用户信息")
	@RequestMapping(value="/info",method=RequestMethod.GET)
	public ResponseEntity<User> userInfo(){
		ResponseEntity<User> response = new ResponseEntity<>();
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		System.out.println(session.getId());
		User user = (User) session.getAttribute("userInfo");
		//生成token
		response.setResult(user);
		response.setCode(Constant.REQUEST_SUCCESS_CODE);
		response.setRemark(Constant.REQUEST_SUCCESS_DESC);
		return response;
	}
}
