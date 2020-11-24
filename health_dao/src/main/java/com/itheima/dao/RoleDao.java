package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 角色持久层接口
 */
public interface RoleDao {

    /**
     * 根据用户id关联查询角色集合
     * @param userId
     * @return
     */
    public Set<Role> findRolesByUserId(Integer userId);


    Page<CheckGroup> selectByCondition(String queryString);

    //新增角色
    int add(Role role);


    //查询角色和权限中间表
    void setRoleAndPermissionIds(Map<String, Integer> map);

    //查询角色和菜单中间表
    void setRoleAndMenu(Map<String, Integer> map);

    int findMax();

    //根据角色id查询角色和权限中间表
    int findCountRoleByPermission(Integer id);

    //根据角色id查询角色和菜单表数据
    int findCountRoleByMenu(Integer id);

    //根据角色id删除角色表记录
    void deleteById(Integer id);

    //根据角色id查询角色信息
    Role findById(Integer roleId);

    //根据角色信息id 查询菜单数据ids
    List<Integer> findMenuIdsByRoleId(Integer roleId);

    //根据角色信息id 查询权限许可ids
    List<Integer> findPermissionIdsByRoleId(Integer roleId);

    //根据角色信息表id 从角色信息和菜单数据的中间表，删除关系数据
    void deleteRMMiddleByRoleId(Integer id);

    //根据角色信息表id 从角色信息和权限许可的中间表，删除关系数据
    void deleteRPMiddleByRoleId(Integer id);

    //根据角色信息id 更新角色信息数据
    void edit(Role role);
}
