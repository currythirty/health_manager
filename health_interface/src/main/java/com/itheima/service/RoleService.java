package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {
    //分页查询
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    //新增角色管理
    void add(Role role, Integer[] menuIds, Integer[] permissionIds);

    //删除角色管理
    void deleteById(Integer id);

    //根据角色id查询角色信息
    Role findById(Integer roleId);

    //根据角色信息id 查询菜单数据ids
    List<Integer> findMenuIdsByRoleId(Integer roleId);

    //根据角色信息id 查询权限许可ids
    List<Integer> findPermissionIdsByRoleId(Integer roleId);

    //编辑角色
    void edit(Role role, Integer[] menuIds, Integer[] permissionIds);
}
