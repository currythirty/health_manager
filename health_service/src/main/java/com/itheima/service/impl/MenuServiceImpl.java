package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.MenuDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Role;
import com.itheima.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao dao;

    @Override
    public PageResult findPage(QueryPageBean parms) {
        PageHelper.startPage(parms.getCurrentPage(),parms.getPageSize());
        Page<Menu> pages = dao.findPage(parms);
        if (pages == null && pages.size()==0){
            pages = dao.queryPage(parms);
        }
        return new PageResult(pages.getTotal(),pages.getResult());
    }

    @Override
    public void addMenu(Map menu) {
        List<Integer> parentMenuId = (List<Integer>) menu.get("parentMenuId");
        if (parentMenuId !=null && parentMenuId.size()==1){
            menu.put("parentMenuId",null);
            menu.put("path",(String)menu.get("priority"));
        }else if(parentMenuId !=null && parentMenuId.size()==2){
            menu.put("parentMenuId", parentMenuId.get(1));
            menu.put("path", "/"+parentMenuId.get(1)+"-"+(String)menu.get("priority"));
        }else if(parentMenuId !=null && parentMenuId.size()>2){
            menu.put("parentMenuId", parentMenuId.get((parentMenuId.size()-1)));
            menu.put("path", "/"+parentMenuId.get(1)+"-"+(String)menu.get("priority"));
        }
        Menu m = new Menu();
        m.setName((String) menu.get("name"));
        m.setLinkUrl((String) menu.get("linkUrl"));
        m.setIcon((String) menu.get("icon"));
        m.setPath((String) menu.get("path"));
        m.setPriority(Integer.parseInt((String) menu.get("priority")));
        m.setParentMenuId((Integer) menu.get("parentMenuId"));
        dao.addMenu(m);
        List<Integer> rolesChecked = (List<Integer>) m.getRolesChecked();
        if (rolesChecked !=null && rolesChecked.size()>0)
        for (Integer i : rolesChecked) {
            Map map = new HashMap();
            map.put("menu_id",m.getId());
            map.put("role_id",i);
            dao.addRoleMenu(map);
        }

    }

    @Override
    public void editMenu(Map menu) {
        List<Integer> parentMenuId = (List<Integer>) menu.get("parentMenuId");
        if (parentMenuId !=null && parentMenuId.size()==1){
            menu.put("parentMenuId",null);
            menu.put("path",(String)menu.get("priority"));
        }else if(parentMenuId !=null && parentMenuId.size()==2){
            menu.put("parentMenuId", parentMenuId.get(1));
            menu.put("path", "/"+parentMenuId.get(1)+"-"+(String)menu.get("priority"));
        }else if(parentMenuId !=null && parentMenuId.size()>2){
            menu.put("parentMenuId", parentMenuId.get((parentMenuId.size()-1)));
            menu.put("path", "/"+parentMenuId.get(1)+"-"+(String)menu.get("priority"));
        }
        dao.editMenu(menu);
        List<Integer> rolesChecked = (List<Integer>) menu.get("rolesChecked");
        if (rolesChecked !=null && rolesChecked.size()>0) {
            dao.deleteRoleMenu((Integer)menu.get("id"));
            for (Integer i : rolesChecked) {
                Map map = new HashMap();
                map.put("menu_id", (Integer)menu.get("id"));
                map.put("role_id", i);
                dao.addRoleMenu(map);
            }
        }else{
            dao.deleteRoleMenu((Integer)menu.get("id"));
        }
    }

    @Override
    public void deleteMenu(Integer id) {
        dao.deleteRoleMenu(id);
        dao.deleteMenu(id);
    }

    @Override
    public Boolean queryParentMenu(Integer id) {
        if (dao.isParentMenu(id) == null){
            if (dao.countParentMenu(id)>0){
                return true;
            }
           return false;
        }
        return false;
    }

    @Override
    public List<Map> findParentMenu() {
        return dao.findParentMenu();
    }

    @Override
    public List<Map> findAllRoles() {
        return dao.findAllRoles();
    }

    @Override
    public List<Integer> queryRolesChecked(Integer id) {
        return dao.queryRolesChecked(id);
    }

    @Override
    public Menu queryMenuById(Integer id) {
        Menu menu = dao.queryMenuById(id);
        if (menu.getParentMenuId()==null){
            menu.setParentMenuId(0);
        }
        return menu;
    }

    @Override
    public List<Map> getMenuByUserName(String username) {
        Jedis jedis = new Jedis("localhost");
        String result = jedis.get(username);
        if (result!=null){
            jedis.close();
            List<Map> list = JSONArray.parseArray(result,Map.class);
            return list;
        }else {
            Integer id = dao.getRole(username);
            List<Map> menu = dao.getMenus(id);
            String s = JSON.toJSONString(menu,true);
            jedis.set(username,s);
            jedis.close();
            return menu;
        }
    }

    @Override
    public List<Menu> findAll() {
        return dao.findAll();
    }
}
