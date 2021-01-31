package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 自定义认证   授权类
 */
@Component
public class UserServiceSecurity implements UserDetailsService {

    @Reference
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户查询用户对象
        com.itheima.pojo.User user = userService.findUserByUsername(username);
        //2.用户对象是否存在，不存在返回null
        if (user == null) {
            return null;//说明账号不存在
        }
        //3.用户存在 获取密码
        String password = user.getPassword();
        //4.根据用户查询出角色 和权限 放到authorities列表中   -------授权
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();//权限列表
        //5.获取用户对象中所有角色
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //角色关键字 放到权限集合中grantedAuthorities
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getKeyword()));//授权

            //6.根据角色获取所有权限
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //普通权限关键字 放到权限集合中grantedAuthorities
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getKeyword()));//授权
            }
        }
        return new User(username, password, grantedAuthorities);
    }


    public static void main(String[] args) {
        List<Student> sl = new ArrayList<>();
        Student student = new Student("小明", 15);
        Student student1 = new Student("小华", 16);
        Student student2 = new Student("小丽", 17);
        sl.add(student);
        sl.add(student1);
        sl.add(student2);
        for (int i = 0; i < sl.size(); i++) {
            if (sl.get(i).getName().equals("小明")) {
                sl.remove(i);
            }
        }

//        Iterator<Student> iterator = sl.iterator();
//        while (iterator.hasNext()) {
//            Student next = iterator.next();
//            if(next.getName().equals("小明")){
//                iterator.remove();
//            }
//        }
        System.out.println(sl.get(1));
    }
}

class Student{
    String name;
    int age;
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}