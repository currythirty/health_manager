package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserRoleService;
import com.itheima.utils.DateUtils;
import com.itheima.utils.MD5Utils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user_role")
public class UserRoleController {

    @Reference
    private UserRoleService userRoleService;
    private  BCryptPasswordEncoder b=new BCryptPasswordEncoder();

    //分页查询用户
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        System.out.println("第一次提交！");
        System.out.println("第二次提交！");
        PageResult pageResult = userRoleService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }


    //新增用户
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody User user, Integer[] roleIds) {

        try {
            String checkPassword = user.getCheckPassword();
            String password = user.getPassword();
            if (!password.equals(checkPassword)) {
                return new Result(false,"两次输入密码不同");
            }

            String username = user.getUsername();
            User user1=userRoleService.findName(username);
            if (user1 != null) {
                return  new Result(false,"用户名已存在,请重新输入！");
            }

            user.setPassword(b.encode(user.getPassword()));
            userRoleService.add(user,roleIds);
            return new Result(true,"新增用户成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"新增用户失败");
        }
    }


    //回显用户管理角色
    @RequestMapping(value = "/findRole", method = RequestMethod.POST)
    public Result findRole() {
        try {
            List<Role> roleList = userRoleService.findRole();
            return new Result(true, "",roleList);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询用户管理角色失败");
        }
    }


    //删除用户
    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    public Result deleteById(Integer id) {
        try {
            userRoleService.deleteById(id);
            return new Result(true, "删除用户成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除用户失败");
        }
    }


    //根据用户ID查询用户
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result findById(Integer userId) {
        try {
            User user = userRoleService.findById(userId);
            return new Result(true, "查询用户成功",user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询用户失败");
        }
    }


    //根据用户id查询用户关联角色id
    @RequestMapping(value = "/findRoleIdsByUserId", method = RequestMethod.GET)
    public List<Integer> findRoleIdsByUserId(Integer userId) {
        return userRoleService.findRoleIdsByUserId(userId);
    }


    //编辑用户
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result edit(@RequestBody User user,Integer[] roleIds) {
        try {
            userRoleService.edit(user,roleIds);
            return new Result(true, "编辑用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"编辑用户失败");
        }
    }


    //修改密码
    @RequestMapping(value = "/updateSecret", method = RequestMethod.POST)
    public Result updateSecret(@RequestBody Map map) {
        try {
            String checkPassword =(String) map.get("checkPassword");
            String newPassword =(String) map.get("newPassword");
            if (!newPassword.equals(checkPassword)) {
                return new Result(false,"两次输入的密码不相同");
            }
            String encode = b.encode(newPassword);
            map.put("encode",encode);
            userRoleService.updateSecret(map);
            return new Result(true,"修改密码成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改密码失败");
        }
    }



}
