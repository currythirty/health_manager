package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限管理控制层
 */
@RestController
@RequestMapping("permission")
public class PermissionController {
    //引用服务
    @Reference
    private PermissionService permissionService;

    /**
     * 权限管理分页查询
     */
    @RequestMapping(value = "findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = permissionService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 新增权限
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody Permission permission) {
        try {
            permissionService.add(permission);
            return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL);
        }
    }

    /**
     * 删除权限
     */
    @RequestMapping(value = "deleteById", method = RequestMethod.GET)
    public Result deleteById(Integer id) {
        try {
            permissionService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_PERMISSION_FAIL);
        }
    }

    /**
     * 根据id查询权限数据
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result findById(Integer id) {
        try {
            Permission permission = permissionService.findById(id);
            return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);

        }
    }

    /**
     * 根据id更新权限
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result edit(@RequestBody Permission permission) {
        try {
            permissionService.edit(permission);
            return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_PERMISSION_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<Permission> permissionList = permissionService.findAll();
            return new Result(true, "查询所有权限成功", permissionList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询所有权限失败");
        }
    }

    /**
     * 查询关联角色
     */
    @RequestMapping(value = "/findRoleName", method = RequestMethod.GET)
    public Result findRoleName(Integer id) {
        try {
            List<String> roleName = permissionService.findRoleName(id);
            return new Result(true, "查询成功", roleName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询失败");
        }
    }
}
