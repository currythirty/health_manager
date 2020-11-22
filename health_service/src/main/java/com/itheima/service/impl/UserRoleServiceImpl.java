package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.UserRoleDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    //分页查询用户
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //第一步：设置分页参数
        PageHelper.startPage(currentPage,pageSize);
        //第二步：查询数据库（代码一定要紧跟设置分页代码  没有手动分页 select * from table where name = 'xx'  ）
        Page<User> UserPage = userRoleDao.selectByCondition(queryString);
        return new PageResult(UserPage.getTotal(),UserPage.getResult());
    }


    //管理角色
    @Override
    public List<Role> findRole() {
        return userRoleDao.findRole();
    }

    //新增用户
    @Override
    public void add(User user, Integer[] roleIds) {
        //第一步：保存检查组表
        userRoleDao.add(user);
        //第二步：获取检查组id
        Integer userId = user.getId();
        //第三步：往检查组检查项中间表 遍历插入关系数据
        setUserAndRole(userId,roleIds);
    }

    //删除用户
    @Override
    public void deleteById(Integer id) {
        //1.根据检查组id查询检查组检查项中间表（count(*)）
        int count1 = userRoleDao.findCountRoleByUserId(id);
        if(count1>0){
            throw new RuntimeException("用户和角色有关联，无法删除！");
        }

        //2.如果第三步关系不存在，根据检查组id查询检查组套餐数据（count(*)）
//        int count2 = userRoleDao.findCountSetmealByGroupId(id);
//        if(count2>0){
//            throw new RuntimeException(MessageConstant.DELETE_SETMEAL_FAIL);
//        }
        //3.根据检查组id删除检查组记录
        userRoleDao.deleteById(id);
    }

    //根据用户id用户
    @Override
    public User findById(Integer userId) {
        return userRoleDao.findById(userId);
    }
    //根据用户id查询关联角色ids
    @Override
    public List<Integer> findRoleIdsByUserId(Integer userId) {
        return userRoleDao.findRoleIdsByUserId(userId);
    }
    //编辑
    @Override
    public void edit(User user, Integer[] roleIds) {
        //1.先根据用户id从用户角色中间表 删除关系数据
        userRoleDao.deleteRelByUserId(user.getId());
        //2.根据页面传入的检查项ids 和 检查组重新建立关系
        setUserAndRole(user.getId(),roleIds);
        //3根据用户id 更新用户数据
        userRoleDao.edit(user);
    }



    //查询是否存在
    @Override
    public User findName(String username) {
        User user=userRoleDao.findName(username);
        return user;
    }

    //修改密码
    @Override
    public void updateSecret(Map map) {
        userRoleDao.updateSecret(map);
    }


    //设置用户和角色中间表
    private void setUserAndRole(Integer userId, Integer[] roleIds) {
        if(roleIds != null && roleIds.length>0){
            for (Integer roleId : roleIds) {
                Map<String,Object> map = new HashMap<>();
                map.put("roleId",roleId);
                map.put("userId",userId);
                userRoleDao.setUserAndRole(map);
            }
        }
    }
}
