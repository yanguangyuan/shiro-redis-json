package com.ygy.shiro.realm;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import com.ygy.shiro.po.User;
import com.ygy.shiro.service.UserService;

/**
* project_name: ltp-web
* package: com.gpl.saas.uc.shiro
* describe: 自定义realm，用于认证与授权
* @author : 严光远
* creat_time: 2018年7月5日 上午10:47:52
* 
**/
public class UserAuthRealm extends AuthorizingRealm {
	
	public static final Logger LOGGER = LogManager.getLogger(UserAuthRealm.class);
	private UserService userService;
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
		Set<String> permissions=new HashSet<String>(3);
		User user = (User) principals.getPrimaryPrincipal();
		String id = user.getId();
		String test = "user:info";
		permissions.add(test);
		//list用add
//		info.addStringPermissions(permissions);
		info.setStringPermissions(permissions);
		return info;
	}

	/** 
	 * 用户认证，未加盐
	 * 
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//获取用户名，由controller层控制不能为null；
		String username=(String) token.getPrincipal();
		User user=new User();
		try {
			user=userService.selectByUsernameOrMobile(username);
		} catch (Exception e) {
			user=null;
			LOGGER.error(e.getMessage());
		}
		//判断用户情况，用户状态，主动抛出异常；
		if(user==null) {
			throw new UnknownAccountException("用户不存在");
		}
		String password=user.getPassword();
		//未加盐
		SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(user, password, getName());
		return info;
	}

}
