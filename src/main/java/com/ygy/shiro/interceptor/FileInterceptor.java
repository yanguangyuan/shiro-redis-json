package com.ygy.shiro.interceptor;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;




public class FileInterceptor extends HandlerInterceptorAdapter{
	/**
	 * 设置允许上传文件的类型列表，
	 */
	private String fileSuffixList;
	/**
	 * 限制文件上传的最大大小，单位为B
	 */
	private long maxSize;
	
	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	public void setFileSuffixList(String fileSuffixList) {
		this.fileSuffixList = fileSuffixList;
	}
	
	@Override
    public boolean preHandle(HttpServletRequest request, 
            HttpServletResponse response, Object handler){
        // 判断是否为文件上传请求
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = 
                    (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> files = 
                                       multipartRequest.getFileMap();
            Iterator<String> iterator = files.keySet().iterator();
            //对多部件请求资源进行遍历
            while (iterator.hasNext()) {
                String formKey = (String) iterator.next();
                MultipartFile multipartFile = 
                              multipartRequest.getFile(formKey);
                String filename=multipartFile.getOriginalFilename();
                //判断是否为限制文件类型
                if (! checkFile(filename)) {
                    //限制文件类型，抛出异常；
                    
                } 
                //判断文件大小；
                Long size=multipartFile.getSize();
                if (size > this.maxSize) {
                    //限制文件大小，抛出异常；
                }
            }
        }
        return true;
    }
    /**
     * 验证文件类型是否正确
     * @author ygy
     * @param fileName
     * @return
     * 2018年8月30日 下午5:10:31
     */
    private boolean checkFile(String fileName) {
        //设置允许上传文件类型
        String suffixList = this.fileSuffixList;
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".")
                + 1, fileName.length());
        if (suffixList.contains(suffix.trim().toLowerCase())) {
            return true;
        }
        return false;
    }
}
