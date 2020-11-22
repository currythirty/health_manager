package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserRoleDao {
    //分页查询用户
    Page<User> selectByCondition(String queryString);

    //管理角色
    List<Role> findRole();

    //添加用户
    void add(User user);
    //设置用户和角色中间表
    void setUserAndRole(Map<String, Object> map);


    //通过用户ID查询管理角色数量
    int findCountRoleByUserId(Integer id);
    //直接删除
    void deleteById(Integer id);


    //通过用户id查询用户
    User findById(Integer userId);
    //根据用户id查询关联角色ids
    List<Integer> findRoleIdsByUserId(Integer userId);
    //先根据用户id从用户角色中间表 删除关系数据
    void deleteRelByUserId(Integer id);
    //根据用户id 更新用户数据
    void edit(User user);


    //查询是否存在
    User findName(String username);

    void updateSecret(Map map);
}
