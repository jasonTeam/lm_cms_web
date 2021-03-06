package com.lm.web.controller;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lm.web.configuration.log.GwsLogger;
import com.lm.web.entity.po.User;
import com.lm.web.entity.vo.UserVO;
import com.lm.web.enums.UserTypeEnum;
import com.lm.web.service.UserRoleService;
import com.lm.web.service.UserService;
import com.lm.web.tools.base.BaseController;
import com.lm.web.tools.constant.CommConstant;
import com.lm.web.tools.result.Ret;
import com.lm.web.tools.validator.ValidatorUtils;
import com.lm.web.tools.validator.group.AddGroup;
import com.lm.web.tools.validator.group.UpdateGroup;



/**
 * 
 * (用户控制类) 
 * @ClassName UserController 
 * @author ShenZiYang 
 * @date 2018年1月6日 下午12:30:09
 */
@RestController
@RequestMapping("/sys/user")
public class UserController extends BaseController{
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleService userRoleService;
	

	/**
	 * 
	 * (获取当前登录用户的信息) 
	 * @Title userInfo 
	 * @return Ret返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月8日下午2:15:18
	 * @throws 异常
	 */
	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public Ret userInfo(){
		GwsLogger.info("获取当前登录用户的信息");
		return Ret.ok().put("user", getUser());
	}
	
	
	/**
	 * 
	 *【分页查询所有用户列表】 
	 * @param vo
	 * @return Ret返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月19日上午10:32:31
	 * @throws 异常
	 */
	@RequestMapping(value = "/list", method  = RequestMethod.GET)
	@RequiresPermissions("sys:user:list")
	public Ret userList(UserVO vo) {
		String code = CommConstant.GWSCOD0000;
		String message = CommConstant.GWSMSG0000;
		Long startTime = System.currentTimeMillis();
		GwsLogger.info("分页查询所有用户列表信息开始:code={},message={},startTime={}",code,message,startTime);
		
		Page<User> pageData = null;
		try{
			pageData = userService.findPageUser(vo, vo.getPageNo()-1, vo.getPageSize(), vo.getSidx());
		}catch(Exception e){
			code = CommConstant.GWSCOD0001;
			message = CommConstant.GWSMSG0001;
			GwsLogger.error("分页查询所有用户列表信息异常:code={},message={},e={}", code, message, e);
		}
		
		Long endTime = System.currentTimeMillis() - startTime;
		GwsLogger.info("分页查询所有用户列表信息结束,code={},message={},endTime={}", code, message,endTime);
		return Ret.ok().put("page", pageData);
	}
	
	
	/**
	 * 
	 *【根据用户ID查询用户信息，用于页面回显】 
	 * @param userId
	 * @return Ret返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月18日下午6:25:54
	 * @throws 异常
	 */
	@RequestMapping(value = "/info/{userId}", method = RequestMethod.GET)
	@RequiresPermissions("sys:user:info")
	public Ret info(@PathVariable("userId") Long userId) {
		String code = CommConstant.GWSCOD0000;
		String message = CommConstant.GWSMSG0000;
		Long startTime = System.currentTimeMillis();
		GwsLogger.info("根据用户ID查询用户信息开始:code={},message={},startTime={}", code, message, startTime);
		
		//参数校验
		if(null != userId || userId <= 0){
			return Ret.error("用户ID不能为空!");
		}
		
		User user = null;
		try {
			user = userService.queryUserByUserId(userId);
			List<Long> roleIdList = userRoleService.queryRoleIdByUserId(userId); // 根据用户ID获取用户所属的角色ID列表
			user.setRoleIdList(roleIdList);
		} catch (Exception e) {
			code = CommConstant.GWSCOD0001;
			message = CommConstant.GWSMSG0001;
			GwsLogger.error("根据用户ID查询用户信息异常:code={},message={},e={}", code, message, e);
		}

		Long endTime = System.currentTimeMillis() - startTime;
		GwsLogger.info("根据用户ID查询用户信息结束:code={},message={},endTime={}", code, message, endTime);
		return Ret.ok().put("user", user);

	}
	
	
	/**
	 * 
	 *【新增用户】
	 * @Title saveUser 
	 * @param user
	 * @return Ret返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月18日下午10:10:43
	 * @throws  异常
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@RequiresPermissions("sys:user:save")
	public Ret saveUser(@RequestBody User user) {
		String code = CommConstant.GWSCOD0000;
		String message = CommConstant.GWSMSG0000;
		Long startTime = System.currentTimeMillis();
		GwsLogger.info("新增用户操作开始:code={},message={},startTime={}", code, message, startTime);

		try {
			ValidatorUtils.validateEntity(user, AddGroup.class);
			userService.saveUser(user);
		} catch (Exception e) {
			code = CommConstant.GWSCOD0001;
			message = CommConstant.GWSMSG0001;
			GwsLogger.error("新增用户操作异常:code={},message={},e={}", code, message, e);
		}

		Long endTime = System.currentTimeMillis() - startTime;
		GwsLogger.info("新增用户操作结束:code={},message={},endTime={}", code, message, endTime);
		return Ret.ok();
	}
	
	
	/**
	 * 
	 *【修改用户信息】
	 * @Title update 
	 * @param user
	 * @return Ret返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月18日下午10:14:27
	 * @throws  异常
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@RequiresPermissions("sys:user:update")
	public Ret update(@RequestBody User user) {
		String code = CommConstant.GWSCOD0000;
		String message = CommConstant.GWSMSG0000;
		Long startTime = System.currentTimeMillis();
		GwsLogger.info("修改用户信息操作开始:code={},message={},startTime={}", code, message, startTime);

		try {
			ValidatorUtils.validateEntity(user, UpdateGroup.class);
			userService.updateUser(user);
		} catch (Exception e) {
			code = CommConstant.GWSCOD0001;
			message = CommConstant.GWSMSG0001;
			GwsLogger.error("修改用户信息操作异常:code={},message={},e={}", code, message, e);
		}

		Long endTime = System.currentTimeMillis() - startTime;
		GwsLogger.info("修改用户信息操作结束:code={},message={},endTime={}", code, message, endTime);
		return Ret.ok();
	}
	
	
	/**
	 * 
	 *【修改密码】
	 * @Title modifyPwd 
	 * @param oriPassword
	 * @param newPassword
	 * @return Ret返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月18日下午10:12:57
	 * @throws  异常
	 */
//	@RequestMapping(value = "/password",method  = RequestMethod.POST)
//	public Ret modifyPwd(String oriPassword, String newPassword) {
//		Assert.isBlank(newPassword, "新密码不为能空");
//		oriPassword = ShiroUtils.sha256(oriPassword, getUser().getSalt()); // 原密码
//		newPassword = ShiroUtils.sha256(newPassword, getUser().getSalt()); // 新密码
//		try {
//			int res = userService.updatePwd(getUserId(), oriPassword, newPassword);
//			if (res < 1) {
//				return Ret.error("密码修改失败!");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return Ret.ok();
//	}
	
	
	/**
	 * 
	 *【批量删除用户,逻辑删除】 
	 * @param userIds
	 * @return R返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月19日上午9:30:37
	 * @throws 异常
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@RequiresPermissions("sys:user:delete")
	public Ret delete(@RequestBody Long[] userIds) {

		String code = CommConstant.GWSCOD0000;
		String message = CommConstant.GWSMSG0000;
		Long startTime = System.currentTimeMillis();
		GwsLogger.info("删除用户操作开始:code={},message={},startTime={}", code, message, startTime);

		if (ArrayUtils.contains(userIds, UserTypeEnum.ADMIN.getVal())) {
			return Ret.error("系统管理员不能删除!");
		}

		if (ArrayUtils.contains(userIds, getUserId())) {
			return Ret.error("当前用户不能删除!");
		}

		try {
			userService.deleteBatchUser(userIds);
		} catch (Exception e) {
			code = CommConstant.GWSCOD0001;
			message = CommConstant.GWSMSG0001;
			GwsLogger.error("删除用户操作异常:code={},message={},e={}", code, message, e);
		}

		Long endTime = System.currentTimeMillis() - startTime;
		GwsLogger.info("删除用户操作结束:code={},message={},endTime={}", code, message, endTime);
		return Ret.ok();
	}

}
