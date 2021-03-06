package com.lm.web.controller;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lm.web.configuration.log.GwsLogger;
import com.lm.web.entity.po.Menu;
import com.lm.web.enums.MenuTypeEnum;
import com.lm.web.service.MenuService;
import com.lm.web.service.ShiroService;
import com.lm.web.tools.base.BaseController;
import com.lm.web.tools.constant.CommConstant;
import com.lm.web.tools.exception.RRException;
import com.lm.web.tools.result.Ret;


/**
 * 
 * (菜单管理) 
 * @ClassName MenuController 
 * @author ShenZiYang 
 * @date 2018年1月6日 上午10:20:17
 */

@RestController
@RequestMapping("/sys/menu")
public class MenuController extends BaseController{
	
	@Autowired
	private MenuService menuService;
	@Autowired
	private ShiroService shiroService;
	
	
	/**
	 * 
	 *【菜单导航】 
	 * @return Ret返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月20日下午5:01:10
	 * @throws 异常
	 */
	@RequestMapping(value = "/nav", method = RequestMethod.GET)
	public @ResponseBody Ret nav() {
		String code = CommConstant.GWSCOD0000;
		String message = CommConstant.GWSMSG0000;
		GwsLogger.info("菜单导航操作开始:code={},message={}", code, message);

		List<Menu> meunList = null;
		Set<String> permissions = null;
		try {
			permissions = shiroService.getUserPermissions(getUserId());
			meunList = menuService.getUserMenuList(getUserId());
		} catch (Exception e) {
			code = CommConstant.GWSCOD0001;
			message = CommConstant.GWSMSG0001;
			GwsLogger.error("菜单导航操作异常:code={},message={},e={}", code, message, e);
			return Ret.error(e.getMessage());
		}

		GwsLogger.info("菜单导航操作结束:code={},message={}", code, message);
		return Ret.ok().put("menuList", meunList).put("permissions", permissions);
	}
	

	
	/**
	 * 
	 *【查询所有的菜单列表】 
	 * @return List<Menu>返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月19日下午12:37:07
	 * @throws 异常
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@RequiresPermissions("sys:menu:list")
	public List<Menu> list() {
		String code = CommConstant.GWSCOD0000;
		String message = CommConstant.GWSMSG0000;
		GwsLogger.info("查询所有的菜单列表开始:code={},message={}", code, message);

		List<Menu> menuList = null;
		try {
			menuList = menuService.menuList();
		} catch (Exception e) {
			code = CommConstant.GWSCOD0001;
			message = CommConstant.GWSMSG0001;
			GwsLogger.error("查询所有的菜单列表异常:code={},message={},e={}", code, message, e);
		}

		GwsLogger.info("查询所有的菜单列表结束:code={},message={}", code, message);
		return menuList;
	}

	/**
	 * 
	 *【选择菜单(添加、修改菜单)】 
	 * @return Ret返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月20日下午5:01:30
	 * @throws 异常
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public Ret select(){
		String code = CommConstant.GWSCOD0000;
		String message = CommConstant.GWSMSG0000;
		GwsLogger.info("新增菜单操作开始:code={},message={}",code,message);
		
		List<Menu> menuList = null;
		try{
			menuList = menuService.queryNotButtonList(); //获取不含按钮的菜单列表
			//添加顶级菜单
			Menu root = new Menu();
			root.setMenuId(0L);
			root.setName("一级菜单");
			root.setParentId(-1L);
			root.setOpen(true);
			menuList.add(root);
		}catch(Exception e){
			code = CommConstant.GWSCOD0001;
			message = CommConstant.GWSMSG0001;
			GwsLogger.error("新增菜单操作异常:code={},message={},e={}", code, message, e);
			return Ret.error(e.getMessage());
		}
		
		GwsLogger.info("新增菜单操作结束:code={},message={}",code,message);
		return Ret.ok().put("menuList", menuList);
	}
	
	
	/**
	 * 
	 * 【菜单信息,用户菜单修改页面回显】
	 * 
	 * @param menuId
	 * @return R返回类型
	 * @author ShenZiYang
	 * @date 2018年1月20日下午5:02:17
	 * @throws 异常
	 */
	@RequestMapping("/info/{menuId}")
	@RequiresPermissions("sys:menu:info")
	public Ret info(@PathVariable("menuId") Long menuId) {
		String code = CommConstant.GWSCOD0000;
		String message = CommConstant.GWSMSG0000;
		GwsLogger.info("用户菜单修改页面回显开始:code={},message={}", code, message);

		Menu menu = null;
		try {
			menu = menuService.queryMenuByMenuId(menuId);
		} catch (Exception e) {
			code = CommConstant.GWSCOD0001;
			message = CommConstant.GWSMSG0001;
			GwsLogger.error("用户菜单修改页面回显异常:code={},message={},e={}", code, message, e);
			return Ret.error(e.getMessage());
		}

		GwsLogger.info("用户菜单修改页面回显结束:code={},message={}", code, message);
		return Ret.ok().put("menu", menu);
	}
	
	
	/**
	 * 
	 *【新增菜单】 
	 * @param menu
	 * @return R返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月20日下午5:08:56
	 * @throws 异常
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:menu:save")
	public Ret save(@RequestBody Menu menu){
		String code = CommConstant.GWSCOD0000;
		String message = CommConstant.GWSMSG0000;
		Long startTime = System.currentTimeMillis();
		GwsLogger.info("新增菜单操作开始:code={},message={},startTime={}",code,message,startTime);
		
		try{
			verifyForm(menu);  //数据校验
			menuService.saveMenu(menu);
		}catch(Exception e){
			code = CommConstant.GWSCOD0001;
			message = CommConstant.GWSMSG0001;
			GwsLogger.error("新增菜单操作异常:code={},message={},e={}", code, message, e);
			return Ret.error(e.getMessage());
		}
		
		Long endTime = System.currentTimeMillis() - startTime;
		GwsLogger.info("新增菜单操作结束:code={},message={},endTime={}",code,message,endTime);
		return Ret.ok();
	}
	

	/**
	 * 
	 *【修改菜单】 
	 * @param menu
	 * @return R返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月20日下午5:09:22
	 * @throws 异常
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:menu:update")
	public Ret update(@RequestBody Menu menu){
		String code = CommConstant.GWSCOD0000;
		String message = CommConstant.GWSMSG0000;
		GwsLogger.info("修改菜单操作开始:code={},message={}",code,message);
		
		try{
			verifyForm(menu); //数据校验
			menuService.updateMenu(menu);
		}catch(RRException e){
			code = CommConstant.GWSCOD0001;
			message = CommConstant.GWSMSG0001;
			GwsLogger.error("修改菜单操作异常:code={},message={},e={}", code, message, e);
			return Ret.error(e.getMessage());
		}
		
		GwsLogger.info("修改菜单操作结束:code={},message={}",code,message);
		return Ret.ok();
	}
	

	/**
	 * 
	 *【删除菜单】 
	 * @param menuId
	 * @return R返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月20日下午5:09:40
	 * @throws 异常
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:menu:delete")
	public Ret delete(long menuId){
		String code = CommConstant.GWSCOD0000;
		String message = CommConstant.GWSMSG0000;
		GwsLogger.info("删除菜单操作开始:code={},message={}",code,message);
		
		//参数校验
		if(menuId <= 31){
			return Ret.error("系统菜单，不能删除");
		}
		
		//删除时判断是否还有子菜单或按钮
		List<Menu> menuList = menuService.queryListParentId(menuId);
		if(null != menuList && menuList.size() > 0){
			return Ret.error("请先删除子菜单或按钮");
		}
		
		try{
			menuService.deleteMenuBatch(new Long[]{menuId});
		}catch(RRException e){
			code = CommConstant.GWSCOD0001;
			message = CommConstant.GWSMSG0001;
			GwsLogger.error("删除菜单操作异常:code={},message={},e={}", code, message, e);
		}
		
		GwsLogger.info("删除菜单操作结束,code={},message={}", code, message);
		return Ret.ok();
	}
	
	
	
	/**
	 * 验证表单提交的参数是否正确
	 */
	private void verifyForm(Menu menu){
		//菜单名称
		if(StringUtils.isBlank(menu.getName())){
			throw new RRException("菜单名称不能为空");
		}
		
		//上级菜单
		if(menu.getParentId() == null){
			throw new RRException("上级菜单不能为空");
		}
		
		//菜单
		if(menu.getType() == MenuTypeEnum.MENU.getVal()){
			if(StringUtils.isBlank(menu.getMenuUrl())){
				throw new RRException("菜单URL不能为空");
			}
		}
		

		//获取上级菜单类型
		int parentType = MenuTypeEnum.CATALOG.getVal();
		if(menu.getParentId() != 0L){
			//根据上级菜单查询菜单实体
			Menu parentMenu = menuService.queryChildMenuId(menu.getParentId());
			parentType = parentMenu.getType();
		}
		
		//目录
		if(MenuTypeEnum.CATALOG.getVal().equals(menu.getType())){
			if(menu.getParentId() != 0){
				throw new RRException("目录的上级只能为一级菜单,请选一级菜单");
			}
			return ;
		}
		
		//菜单
		if (MenuTypeEnum.MENU.getVal().equals(menu.getType())) {
			//如果选中的是一级菜单 type设置为 -1
			if(menu.getParentName().equals("一级菜单")){
				parentType = -1;
			}
			if (!MenuTypeEnum.CATALOG.getVal().equals(parentType)) {
				throw new RRException("菜单的上级只能为目录,请选目录");
			}
		}
		
		// 按钮
		if (MenuTypeEnum.BUTTON.getVal().equals(menu.getType())) {
			if (parentType != MenuTypeEnum.MENU.getVal()) {
				throw new RRException("按钮的上级只能为菜单,请选菜单!");
			}
			return;
		}
	}
	
	
	
}
