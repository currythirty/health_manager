package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

import java.util.List;
import java.util.Set;

/**
 * 权限持久层接口
 */
public interface PermissionDao {

    /**
     * 根据角色id关联查询权限集合
     * @param roleId
     * @return
     */
    public Set<Permission> findPermissionsByRoleId(Integer roleId);

    List<Permission> findAll();

    /**
     * 根据权限id 查询权限角色中间表
     */
    int findCountByPermissionId(Integer id);

    /**
     * 权限管理分页查询
     * @param queryString
     * @return
     */
    Page<Permission> selectByCondition(String queryString);

    /**
     * 新增权限
     * @param permission
     */
    void add(Permission permission);

    /**
     * 删除权限
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 根据id查询权限数据
     * @param id
     * @return
     */
    Permission findById(Integer id);

    /**
     * 根据id更新权限数据
     * @param permission
     */
    void edit(Permission permission);


}
