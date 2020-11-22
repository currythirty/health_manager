package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserRoleService {

     PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

     //查询管理角色
     List<Role> findRole();

     //新增用户
     void add(User user, Integer[] roleIds);

     //删除用户
     void deleteById(Integer id);


     //通过id查询用户
     User findById(Integer userId);
     //通过用户id查询关联角色id
     List<Integer> findRoleIdsByUserId(Integer userId);
     //编辑用户
     void edit(User user, Integer[] roleIds);


     User findName(String username);

     void updateSecret(Map map);
}
