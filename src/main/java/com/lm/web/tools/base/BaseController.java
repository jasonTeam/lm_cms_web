package com.lm.web.tools.base;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lm.web.entity.po.User;


/**
 * 
 * (Controller公共组件) 
 * @ClassName AbstractController 
 * @author ShenZiYang 
 * @date 2018年1月6日 下午2:46:33
 */

public abstract class BaseController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	

	//获取用户实体
	protected User getUser() {
		return (User) SecurityUtils.getSubject().getPrincipal();
	}
	
	//获取用户Id
	protected Long getUserId() {
		logger.info("当前登录用户ID为:"+getUser().getUserId());
		return getUser().getUserId();
	}
	
	//获取部门ID
	protected Long getDeptId() {
		return getUser().getDeptId();
	}
	
}
