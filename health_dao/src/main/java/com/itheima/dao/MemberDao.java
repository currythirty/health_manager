package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    public List<Member> findAll();
    public Page<Member> selectByCondition(String queryString);
    public void add(Member member);
    public void deleteById(Integer id);
    public Member findById(Integer id);
    public Member findByTelephone(String telephone);
    public void edit(Member member);

    /**
     * 会员数量折线图
     * @param date
     * @return
     */
    public Integer findMemberCountByMonth(String date);

    public Integer findMemberCountBeforeDate(String date);



    public Integer findMemberCountByDate(String date);
    public Integer findMemberCountAfterDate(String date);
    public Integer findMemberTotalCount();


    //查询会员性别和对应的会员数量
    List<Map> findMemberNumberBySex();


    //查询所有会员的出生日期
    List<String> findMemberBrithday();

}
