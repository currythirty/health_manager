package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.Date;
import java.util.Map;

/**
 * 会员服务接口
 */
public interface MemberService {
    /**
     * 根据手机号码查询会员信息
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 自动注册方法
     * @param member
     */
    void add(Member member);
    /**
     * 会员数量折线图
     */
    Map<String,Object> getMemberReport(Date start,Date end);

    /*
     * 会员数量占比饼图
     *
     * */
    Map<String, Object> getMemberNumberReport() throws Exception;
    Map<String, Object> getYearMemberReport();
}
