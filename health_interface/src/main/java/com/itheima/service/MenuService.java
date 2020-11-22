package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService {
    PageResult findPage(QueryPageBean parms);

    void addMenu(Map menu);

    void editMenu(Map menu);

    void deleteMenu(Integer id);

    Boolean queryParentMenu(Integer id);

    List<Map> findParentMenu();

    List<Map> findAllRoles();

    List<Integer> queryRolesChecked(Integer id);

    Menu queryMenuById(Integer id);

    List<Map> getMenuByUserName(String username);

    List<Menu> findAll();
}
