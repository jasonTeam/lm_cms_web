package com.lm.web.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lm.web.entity.po.Role;
import com.lm.web.entity.vo.RoleVO;


/**
 * 
 * 角色业务层接口
 * @ClassName RoleService 
 * @author ShenZiYang 
 * @date 2018年1月11日 下午4:47:44
 */
public interface RoleService {
	
	/**
	 * 
	 * (分页查询角色列表) 
	 * @Title findPageRole 
	 * @param pageNo
	 * @param pageSize
	 * @param sortField
	 * @return Page<Role>返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月11日下午4:49:13
	 * @throws 查询异常
	 */
	Page<Role> findPageRole(RoleVO vo,Integer pageNo,Integer pageSize,String sortField);
	
	/**
	 * 
	 * (获取所有角色列表集合) 
	 * @Title getRoleList 
	 * @return List<Role>返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月13日下午4:16:43
	 * @throws 异常
	 */
	List<Role> getRoleList();
	
	/**
	 * 
	 * (根据角色ID获取角色实体) 
	 * @Title queryByRoleId 
	 * @param roleId
	 * @return Role返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月13日下午4:30:33
	 * @throws 异常
	 */
	Role queryByRoleId(Long roleId);
	
	/**
	 * 
	 *【修改角色】
	 * @Title updateRole 
	 * @param role void返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月19日下午9:44:17
	 * @throws  异常
	 */
	void updateRole(Role role);
	
	/**
	 * 
	 *【新增角色】
	 * @Title saveRole 
	 * @param role void返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月20日下午10:33:20
	 * @throws  异常
	 */
	void saveRole(Role role);
	
	/**
	 * 
	 *【删除角色 -- 批量删】
	 * @Title deleteRoleBatch 
	 * @param roleIds void返回类型   
	 * @author ShenZiYang
	 * @date 2018年1月19日下午9:54:57
	 * @throws  异常
	 */
	void deleteRoleBatch(Long[] roleIds);
	
	
	
	
}
