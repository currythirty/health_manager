package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Permission;

/**
 * 权利管理服务接口
 */
public interface PermissionService {
    //分页查询
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    //新增权限
    void add(Permission permission);

    //删除权限
    void deleteById(Integer id);

    //根据权限id查询权限数据
    Permission findById(Integer id);

    //根据id更新权限
    void edit(Permission permission);
}
