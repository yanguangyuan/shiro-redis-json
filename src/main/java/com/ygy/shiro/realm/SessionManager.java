package com.ygy.shiro.realm;

import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
* project_name: ltp-web
* package: com.gpl.saas.uc.shiro
* describe: shiro的sessionid获取
* @author : 严光远
* creat_time: 2018年7月5日 上午10:47:16
* 
**/
public class SessionManager extends DefaultWebSessionManager {

    private static final String TOKEN = "Token";

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        return WebUtils.toHttp(request).getHeader(TOKEN);
    }

}
