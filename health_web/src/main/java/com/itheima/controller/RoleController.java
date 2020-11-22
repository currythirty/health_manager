package com.itheima.controller;



import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * 角色管理控制层
 * */
@RestController
@RequestMapping("/rolemanagement")
public class RoleController {

    @Reference
    private RoleService roleService;

    /**
     * 检查分页查询
     */
  @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
    PageResult pageResult =roleService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
    return pageResult;
  }



    /**
     * 新增角色管理
     */
    @RequestMapping(value = "/add")
    public Result add(@RequestBody Role role,Integer[] menuIds,Integer[] permissionIds){
        try {

            roleService.add(role,menuIds,permissionIds);
            return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ROLE_FAIL);
        }
    }

    /**
     * 删除角色管理
     */
    @RequestMapping(value = "deleteById",method = RequestMethod.GET)
    public Result deleteById(Integer id){

        try {
            roleService.deleteById(id);
            return new Result(true,"删除角色成功");
        }catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除角色失败");
        }
    }

    /**
     * 根据角色id查询角色信息
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer roleId){
        try {
            Role role = roleService.findById(roleId);
            return new Result(true,"查询角色id成功",role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询角色id失败");
        }
    }

    /**
     * 根据角色信息id 查询菜单数据ids
     */
    @RequestMapping(value = "/findMenuIdsByRoleId",method = RequestMethod.GET)
    public List<Integer> findMenuIdsByRoleId(Integer roleId){
        return roleService.findMenuIdsByRoleId(roleId);
    }

    /**
     * 根据角色信息id 查询权限许可ids
     */
    @RequestMapping(value = "findPermissionIdsByRoleId",method = RequestMethod.GET)
    public List<Integer> findPermissionIdsByRoleId(Integer roleId){
        return roleService.findPermissionIdsByRoleId(roleId);
    }

    /**
     * 编辑角色
     */
    @RequestMapping(value = "edit",method = RequestMethod.POST)
    public Result edit(@RequestBody Role role,Integer[] menuIds,Integer[] permissionIds){
        try {
            roleService.edit(role,menuIds,permissionIds);
            return new Result(true,"编辑角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"编辑角色失败");
        }
    }
}
