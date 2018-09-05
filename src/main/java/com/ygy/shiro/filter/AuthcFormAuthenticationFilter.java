package com.ygy.shiro.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygy.shiro.common.Constant;
import com.ygy.shiro.common.ResponseEntity;
import com.ygy.shiro.po.User;
import com.ygy.shiro.service.UserService;




public class AuthcFormAuthenticationFilter extends FormAuthenticationFilter {
	public static final Logger LOGGER = LogManager.getLogger(AuthcFormAuthenticationFilter.class);
	private UserService userService;
	
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		//如果认证状态为true将放行，为false执行onAccessDenied方法
		Subject subject = getSubject(request, response);
		HttpServletRequest re = (HttpServletRequest) request;
		String requestUrl=re.getRequestURL().toString();
		System.out.println(requestUrl);
		System.out.println(re.getRemoteHost());
		boolean status=subject.isAuthenticated();
		if(status) {
			User user = (User) subject.getPrincipal();
			System.out.println(user.getUsername());
		}
		return status;
	}
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		//如果是登录
		if (isLoginRequest(request, response) && isLoginSubmission(request, response)) {
            return executeLogin(request, response);
        }
		//如果有token但是没有认证成功
		HttpServletRequest req = (HttpServletRequest) request;
		ResponseEntity res = new ResponseEntity<>();
		String token=req.getHeader(Constant.TOKEN);
		if(token!=null) {
			res.setCode(Constant.LOGIN_EXPIRE_CODE);
			res.setRemark(Constant.LOGIN_EXPIRE_DESC);
		}else {
			res.setCode(Constant.NO_PERMISSION_CODE);
			res.setRemark(Constant.NO_PERMISSION_DESC);
		}
		writeJsonResult(res, response);
		return false;
	}
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		//��¼��֤�ɹ�
		ResponseEntity<String> res = new ResponseEntity<>();
		
//		String result = UUID.randomUUID().toString();
		Session session = subject.getSession();
		User user=(User) subject.getPrincipal();
		session.setAttribute("userInfo", user);
		System.out.println(session.getId());
		
		String tokenInfo = "";
		//生成token,使用token代替cookie;
		try {
			tokenInfo=session.getId().toString();
		}catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		res.setResult(tokenInfo);
		res.setCode(Constant.REQUEST_SUCCESS_CODE);
		res.setRemark(Constant.REQUEST_SUCCESS_DESC);
		writeJsonResult(res, response);
		return false;
	}
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		//登录失败
		ResponseEntity res = new ResponseEntity<>();
		if(e instanceof UnknownAccountException||e instanceof IncorrectCredentialsException) {
			res.setRemark("用户名或密码错误");
		}else if(e instanceof DisabledAccountException) {
			res.setRemark("用户已被禁用");
		}else if(e instanceof Exception) {
			res.setRemark("登录错误");
		}
		res.setCode(Constant.REQUEST_FAIL_CODE);
		writeJsonResult(res, response);
		return false;
	}
	@Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		//创建认证token信息（json交互）
        String host = this.getHost(request);
        String username = this.getUsername(request);
        String password = this.getPassword(request);
        boolean rememberMe = this.isRememberMe(request);
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        if ( username == null && password == null ) {
            JSONObject loginObject = getRequestBody(httpServletRequest);
            if ( loginObject != null ) {
                username = loginObject.getString(this.getUsernameParam());
                password = loginObject.getString(this.getPasswordParam());
                if ( loginObject.containsKey(this.getRememberMeParam()) ) {
                    rememberMe = loginObject.getBoolean(this.getRememberMeParam());
                }

            }
        }
        return this.createToken(username, password, rememberMe, host);

    }
	
	/**
	 * 结果信息已json格式返回
	 * @author ygy
	 * @param result
	 * @param response
	 * 2018年9月5日 下午3:18:28
	 */
	private void writeJsonResult(ResponseEntity result, ServletResponse response) {

        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json");
        String json = JSON.toJSONString(result);
        try (PrintWriter pw = httpResponse.getWriter()) {
            pw.write(json);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 从请求信息转化的字符串信息中，获取json对象
     * @author ygy
     * @param request
     * @return
     * 2018年9月5日 下午3:18:48
     */
    private JSONObject getRequestBody(HttpServletRequest request) {

        try {

            String json = getRequestBodyString(request);
            if (!StringUtils.isEmpty(json)) {
                return JSONObject.parseObject(json);
            }
        } catch (Exception e){
        	e.printStackTrace();
        	LOGGER.error("Form Auth Filter GetRequestBody Error ", e);
        }

        return null;
    }

    
    /**
     * 将请求信息转为字符串，request.getReader()只能获取一次；
     * @author ygy
     * @param request
     * @return
     * @throws IOException
     * 2018年9月5日 下午3:19:29
     */
    private String getRequestBodyString(HttpServletRequest request) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        try (BufferedReader reader = request.getReader()) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }
        }
        return stringBuffer.toString();
    }
}
