package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.RoleDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;


    //分页
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //第一步：设置分页参数
        PageHelper.startPage(currentPage,pageSize);
        //第二步：查询数据库
        Page<CheckGroup> rolePage = roleDao.selectByCondition(queryString);
        return new PageResult(rolePage.getTotal(),rolePage.getResult());
    }

    //新增角色
    @Override
    public void add(Role role,Integer[] menuIds,Integer[] permissionIds) {
              //添加角色信息
                roleDao.add(role);
              // 添加角色菜单数据
              setRoleAndMenu(role.getId(),menuIds);
              //添加角色权限管理
              setRoleAndPermissionIds(role.getId(),permissionIds);



    }

    @Override
    public void deleteById(Integer id) {
        //1.根据角色id查询角色和权限中间表
        int count1 = roleDao.findCountRoleByPermission(id);
        if (count1 > 0) {
            throw new RuntimeException("权限表已经关联角色表无法删除");
        }
        //2.根据角色id查询角色和菜单表数据
        int count2 = roleDao.findCountRoleByMenu(id);
        if (count2 > 0) {
            throw new RuntimeException("菜单表已经关联角色表无法删除");
        }
        //3.根据角色id删除角色表记录
        roleDao.deleteById(id);
    }

    //根据角色id查询角色信息
    @Override
    public Role findById(Integer roleId) {
        return roleDao.findById(roleId);
    }

    //根据角色信息id 查询菜单数据ids
    @Override
    public List<Integer> findMenuIdsByRoleId(Integer roleId) {
        return roleDao.findMenuIdsByRoleId(roleId);
    }

    //根据角色信息id 查询权限许可ids
    @Override
    public List<Integer> findPermissionIdsByRoleId(Integer roleId) {
        return roleDao.findPermissionIdsByRoleId(roleId);
    }

    //编辑角色
    @Override
    public void edit(Role role, Integer[] menuIds, Integer[] permissionIds) {
        //1.根据角色信息表id 从角色信息和菜单数据的中间表，删除关系数据,Role=R,Menu=M,中间=Middle
        roleDao.deleteRMMiddleByRoleId(role.getId());
        //2.根据页面传入的菜单数据ids和角色信息重新建立关系
        setRoleAndMenu(role.getId(),menuIds);
        //3.根据角色信息表id 从角色信息和权限许可的中间表，删除关系数据，Role=R,Permission,中间=Middle
        roleDao.deleteRPMiddleByRoleId(role.getId());
        //4.根据页面传入的权限许可ids和角色信息重新建立关系
        setRoleAndPermissionIds(role.getId(),permissionIds);
        //5.根据角色信息id 更新角色信息数据
        roleDao.edit(role);
    }



    private void setRoleAndPermissionIds(Integer roleId, Integer[] permissionIds) {
        if (permissionIds != null && permissionIds.length>0) {
            for (Integer permissionId : permissionIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("permissionId",permissionId);
                map.put("roleId",roleId);
                roleDao.setRoleAndPermissionIds(map);
            }
        }
    }

    private void setRoleAndMenu(Integer roleId, Integer[] menuIds) {
        if (menuIds != null && menuIds.length>0) {
            for (Integer menuId : menuIds) {
                Map<String,Integer> map  = new HashMap<>();
                map.put("menuId",menuId);
                map.put("roleId",roleId);
                roleDao.setRoleAndMenu(map);
            }
        }
    }

}
