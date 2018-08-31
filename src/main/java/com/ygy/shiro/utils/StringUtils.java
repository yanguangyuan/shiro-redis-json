package com.ygy.shiro.utils;

public class StringUtils {
	/**
	 * 判断字符串是否为空或去掉前后空格后为“”
	 * @author ygy
	 * @param str
	 * @return
	 * 2018年8月30日 下午5:42:37
	 */
	public static boolean isEmpty(String str) {
		if(str==null||str.trim().length()==0) {
			return true;
		}else {
			return false;
		}
	}
}
