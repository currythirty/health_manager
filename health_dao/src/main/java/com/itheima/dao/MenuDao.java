package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface MenuDao {

    Page<Menu> findPage(QueryPageBean pageBean);

    Page<Menu> queryPage(QueryPageBean pageBean);

    void addMenu(Menu menu);

    void addRoleMenu(Map map);

    void editMenu(Map menu);

    void deleteRoleMenu(Integer id);

    void deleteMenu(Integer id);

    @Select("select parentMenuId from t_menu where id = #{id}")
    Integer isParentMenu(Integer id);

    @Select("select count(*) from t_menu where parentMenuId = #{id}")
    Integer countParentMenu(Integer id);

    List<Map> findParentMenu();

    List<Map> findAllRoles();

    List<Integer> queryRolesChecked(Integer id);

    @Select("select * from t_menu where id = #{id}")
    Menu queryMenuById(Integer id);

    @Select("select r.id id from t_role r,t_user_role ur,t_user u where u.username = #{username} and u.id = ur.user_id and ur.role_id = r.id")
    Integer getRole(String username);

    List<Map> getMenus(Integer id);


    List<Menu> findAll();

    @Select("select path from t_menu where id = #{integer}")
    String queryPath(Integer integer);

}
