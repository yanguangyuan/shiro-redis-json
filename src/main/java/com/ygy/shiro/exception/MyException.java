package com.ygy.shiro.exception;

/**
 * project_name: shiro-redis-json 
 * package: com.ygy.shiro.exception 
 * describe:基础异常，继承至运行时异常，不用在编译时主动抛出，而是在运行时抛出，方便自定义异常处理捕捉，减少代码
 * 
 * @author : 严光远 creat_time: 2018年9月5日 上午10:19:06
 * 
 **/
public class MyException extends RuntimeException {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 5301201923100353477L;
	/**
	 * 异常信息
	 */
	private String errorMsg;


	public MyException(String message) {
		super(message);
	}

	public MyException(Throwable cause) {
		super(cause);
	}

	public MyException(String message, Throwable cause) {
		super(message, cause);
	}
	public String getErrorMsg() {
		return this.errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
