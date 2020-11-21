package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService service;

    /*分页查询*/
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean parms){
        try {
            return service.findPage(parms);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /*新增菜单*/
    @RequestMapping("/add")
    public Result addMenu(@RequestBody Map menu){
        try {
            service.addMenu(menu);
            //清空redis缓存
            cleanRedis();
            return new Result(true,"新增菜单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"新增菜单失败");
        }
    }

    /*修改菜单*/
    @RequestMapping("/edit")
    public Result editMenu(@RequestBody Map menu){
        try {
            service.editMenu(menu);
            //清空redis缓存
            cleanRedis();
            return new Result(true,"修改菜单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改菜单失败");
        }
    }

    /*删除菜单*/
    @RequestMapping("/deleteById")
    public Result deleteMenu(Integer id){
        try {
            Boolean result = service.queryParentMenu(id);
            if (result){
                return new Result(false,"请将该父菜单下的子菜单移除后，再进行删除操作");
            }
            service.deleteMenu(id);
            //清空redis缓存
            cleanRedis();
            return new Result(true,"删除菜单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除菜单失败");
        }
    }

    /*查询父菜单*/
    @RequestMapping("/findParentMenu")
    public Result findParentMenu(){
        try {
            List<Map> list= service.findParentMenu();
            List<Map> result = new ArrayList<>();
            Map map = new HashMap();
            map.put("label","一级菜单");
            map.put("value",0);
            result.add(map);
            Map map2 = new HashMap();
            map2.put("label","二级菜单");
            map2.put("children",list);
            result.add(map2);
            return new Result(true,"查询父菜单成功",result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询父菜单失败");
        }
    }

    /*查询角色*/
    @RequestMapping("/findAllRoles")
    public Result findAllRoles(){
        try {
            List<Map> list= service.findAllRoles();
            return new Result(true,"查询成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }

    /*查询角色选择情况*/
    @RequestMapping("/queryRolesChecked")
    public Result queryRolesChecked(Integer id){
        try {
            List<Integer> list= service.queryRolesChecked(id);
            return new Result(true,"查询成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }

    /*查询角色选择情况*/
    @RequestMapping("/queryMenuById")
    public Result queryMenuById(Integer id){
        try {
            Menu menu= service.queryMenuById(id);
            return new Result(true,"查询成功",menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }

    /*按照权限获取菜单*/
    @RequestMapping("/getMenuByUserName")
    public Result getMenuByUserName(String username){
        try {
            List<Map> menu = service.getMenuByUserName(username);
            return new Result(true,"查询成功",menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }

    /*删除redis内的用户对应的菜单数据*/
    private void cleanRedis(){
        Jedis redis = new Jedis();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        redis.set(username,"");
        redis.close();
    }

}
