package com.ygy.shiro.common;

/**
* project_name: shiro-redis-json
* package: com.ygy.shiro.common
* describe: 常量控制
* @author : ygy
* creat_time: 2018年8月31日 下午5:22:01
* 
**/
public class Constant {
	/**
	 * 访问失败码 -- 50001；
	 */
	public static final int REQUEST_FAIL_CODE=50001;
	/**
	 * 访问失败描述 --访问失败
	 */
	public static final String REQUEST_FAIL_DESC="访问失败";
	/**
	 * 访问成功码 -- 10000；
	 */
	public static final int REQUEST_SUCCESS_CODE=10000;
	/**
	 * 访问成功描述 -- 操作成功
	 */
	public static final String REQUEST_SUCCESS_DESC="操作成功";
	/**
	 * 没有权限码 -- 1003；
	 */
	public static final int NO_PERMISSION_CODE=1003;
	/**
	 * 没有权限描述 -- 您没有权限访问
	 */
	public static final String NO_PERMISSION_DESC="您没有权限访问";
	/**
	 * 登录到期失效码 -- 1009
	 */
	public static final int LOGIN_EXPIRE_CODE=1009;
	/**
	 * 登录失效描述 -- 登录已失效，请重新登录
	 */
	public static final String LOGIN_EXPIRE_DESC="登录已失效，请重新登录";
}
