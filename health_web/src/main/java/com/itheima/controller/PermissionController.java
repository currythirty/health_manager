package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.Result;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Permission> permissionList = permissionService.findAll();
            return new Result(true,"查询所有权限成功",permissionList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询所有权限失败");
        }
    }

}
