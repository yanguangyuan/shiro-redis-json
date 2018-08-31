package com.ygy.shiro.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
* project_name: shiro-redis-json
* package: com.ygy.shiro.common
* describe: 返回值封装
* @author : ygy
* creat_time: 2018年8月31日 下午3:40:42
* 
**/
public class ResponseEntity<T> implements Serializable{

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3323579457573437064L;
	private String requestId = UUID.randomUUID().toString();
	  private long reqTime = System.currentTimeMillis();
	  private long resTime = -9177275398407323648L;
	  private T result;
	  private int code;
	  private String remark;
	  private Map<String, Object> params = new HashMap<>();

	  public Map<String, Object> getParams()
	  {
	    return this.params;
	  }

	  public ResponseEntity setParams(Map<String, Object> params) {
	    this.params = params;
	    return this;
	  }

	  public ResponseEntity addParam(String key, Object value) {
	    getParams().put(key, value);
	    return this;
	  }

	  public ResponseEntity removeParam(String key) {
	    getParams().remove(key);
	    return this;
	  }

	  public Object getResult() {
	    return this.result;
	  }

	  public ResponseEntity<T> setResult(T result)
	  {
	    this.result = result;
	    return this;
	  }

	  public long getReqTime() {
	    return this.reqTime;
	  }

	  public ResponseEntity<T> setReqTime(long reqTime) {
	    this.reqTime = reqTime;
	    return this;
	  }

	  public long getResTime() {
	    return this.resTime;
	  }

	  public ResponseEntity<T> setResTime(long resTime) {
	    this.resTime = resTime;
	    return this;
	  }

	  public String getRequestId()
	  {
	    return this.requestId;
	  }

	  public ResponseEntity setRequestId(String requestId) {
	    this.requestId = requestId;
	    return this;
	  }

	  public String getRemark() {
	    return this.remark;
	  }

	  public ResponseEntity setRemark(String remark) {
	    this.remark = remark;
	    return this;
	  }

	  public int getCode()
	  {
	    return this.code;
	  }

	  public ResponseEntity setCode(int code) {
	    this.code = code;
	    return this;
	  }

	  public String toString()
	  {
	    return "ResponseEntity{requestId='" + this.requestId + '\'' + ", reqTime=" + this.reqTime + ", resTime=" + this.resTime + ", result=" + this.result + ", code=" + this.code + ", remark='" + this.remark + '\'' + ", params=" + this.params  + '}';
	  }
}
