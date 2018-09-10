package com.ygy.shiro.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.ygy.shiro.exception.MyException;
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
@Api(value = "用户控制器",description = "用于用户登录等控制器")
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
	@ApiOperation(value="登录",notes="登录接口描述")
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ResponseEntity<String> login(@RequestBody UserLoginInfo info){
		ResponseEntity<String> response = new ResponseEntity<>();
		response.setCode(Constant.REQUEST_FAIL_CODE);
		response.setRemark("系统错误，shiro没生效");
		return response;
	}
	/**
	 * shiro session信息获取
	 * @author 严光远
	 * @return
	 * 2018年9月5日 上午10:35:53
	 */
	@ResponseBody
	@RequiresPermissions({"user:info"})
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
	@ResponseBody
	@ApiOperation(value="异常测试")
	@RequestMapping(value="/exceptionTest",method=RequestMethod.GET)
	public ResponseEntity<User> userExceptionTest(){
		ResponseEntity<User> response = new ResponseEntity<>();
		throw new MyException("异常测试");
//		return response;
	}
	/**
	 * 权限测试，此权限不存在
	 * @author 严光远
	 * @return
	 * 2018年9月5日 上午10:35:42
	 */
	@ResponseBody
	@RequiresPermissions({"user:test"})
	@ApiOperation(value="权限测试")
	@RequestMapping(value="/authorizateTest",method=RequestMethod.GET)
	public ResponseEntity<User> userAuthorizateTest(){
		ResponseEntity<User> response = new ResponseEntity<>();
		return response;
	}
	/**
	 * 退出测试，只是做一个swagger接口
	 * @author 严光远
	 * @return
	 * 2018年9月5日 上午10:35:42
	 */
	@ResponseBody
	@ApiOperation(value="退出测试")
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public ResponseEntity<User> logout(){
		ResponseEntity<User> response = new ResponseEntity<>();
		return response;
	}
}
