package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.PermissionDao;
import com.itheima.entity.PageResult;

import com.itheima.dao.PermissionDao;

import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.Set;

/**
 *  权限管理服务接口实现类
 */

import java.util.List;



@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    /**
     * 权限管理分页查询
     *
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //1.设置分页参数
        PageHelper.startPage(currentPage, pageSize);
        //2.查询数据库(代码一定要紧跟设置分页代码 没有手动分页 select * from table where name = 'xx')
        Page<Permission> permissionPage = permissionDao.selectByCondition(queryString);
        return new PageResult(permissionPage.getTotal(),permissionPage.getResult());
    }

    /**
     * 新增权限
     * @param permission
     */
    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    /**
     * 删除权限
     * @param
     */
    @Override
    public void deleteById(Integer id) {
        //1.根据权限id查询  权限角色中间表
        int count = permissionDao.findCountByPermissionId(id);

        //2.第二步：如果中间表有记录 抛出异常 controller捕获异常 返回告知用户
        if (count > 0) {
            throw new RuntimeException(MessageConstant.DELETE_PERMISSION_FAIL2);
        }
        //3.中间表没有记录，根据权限id删除
        permissionDao.deleteById(id);
    }

    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }

    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();

    }
}
