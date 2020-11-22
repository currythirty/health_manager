package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.DateUtils;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 会员服务实现
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    /**
     * 根据手机号码查询会员信息
     *
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 自动注册方法
     *
     * @param member
     */
    @Override
    public void add(Member member) {
        //密码
        String password = member.getPassword();
        if (!StringUtils.isEmpty(password)) {
            //加密 MD5
            String md5Pwd = MD5Utils.md5(password);
            member.setPassword(md5Pwd);
        }
        memberDao.add(member);
    }

    /**
     * 会员数量折线图
     */
    @Override
    public Map<String, Object> getMemberReport(Date start, Date end) {
        //获取年份的差
        int year = end.getYear() - start.getYear();
        //获取月份的差
        int month01 = start.getMonth();
        int month02 = end.getMonth();
        Integer themonth = month02 - month01;
        Integer allmonth = allmonth = year * 12 + themonth;
        //定义返回结果Map
        Map<String, Object> rsMap = new HashMap<>();

        //1.key:months—[2020-01,2020-02…最近一年]  --List<String>
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(end);
        calendar.add(Calendar.MONTH, -allmonth);
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= allmonth; i++) {
            //遍历获取每一个月年月 ，并将年月放入List<String>集合中
            months.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
            calendar.add(Calendar.MONTH, 1);
            rsMap.put("months", months);
        }
            //key:memberCount—[50,100,…最近一年]       List<Integer>
            //select count(*) from t_member where regTime <= ‘2020-01-31’
            List<Integer> memberCount = new ArrayList<>();
            if (months != null && months.size() > 0) {
                for (String month : months) {
                    //month 2020-07 ==> 2020-07-31
                    String newMonth = month + "-31";
                    Integer mc = memberDao.findMemberCountByMonth(newMonth);

                    memberCount.add(mc);
                }
            }
            rsMap.put("memberCount", memberCount);
            return rsMap;
        }

        @Override
        public Map<String, Object> getYearMemberReport () {
            //定义返回结果Map
            Map<String, Object> rsMap = new HashMap<>();

            //1.key:months—[2020-01,2020-02…最近一年]  --List<String>
            Calendar calendar1 = Calendar.getInstance();
            calendar1.add(Calendar.MONTH, -12);
            //遍历获取每一个月年月 ，并将年月放入List<String>集合中
            List<String> months1 = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                months1.add(new SimpleDateFormat("yyyy-MM").format(calendar1.getTime()));
                calendar1.add(Calendar.MONTH, 1);
            }

            rsMap.put("months", months1);

            //key:memberCount—[50,100,…最近一年]       List<Integer>
            //select count(*) from t_member where regTime <= ‘2020-01-31’
            List<Integer> memberCount = new ArrayList<>();
            if (months1 != null && months1.size() > 0) {
                for (String month : months1) {
                    //month 2020-07 ==> 2020-07-31
                    String newMonth = month + "-31";
                    Integer mc = memberDao.findMemberCountByMonth(newMonth);
                    memberCount.add(mc);
                }
            }
            rsMap.put("memberCount", memberCount);
            return rsMap;
        }

        /************************************************************************************************************/


        /*
         * 会员数量占比饼图
         *
         * */
        @Override
        public Map<String, Object> getMemberNumberReport () throws Exception {
            //1. 创建Map、List对象，存放返回结果
            Map mbrMap = new HashMap();
            //2. 调用服务，查询性别、及其会员数量
            List<Map> sexMap = memberDao.findMemberNumberBySex();

            mbrMap.put("sexMap", sexMap);
            //3. 创建List<String> 存放性别
            List<String> sexNames = new ArrayList<>();
            if (sexMap != null && sexMap.size() > 0) {
                //遍历集合，获取所有的性别
                for (Map sexName : sexMap) {
                    String name = (String) sexName.get("name");
                    if ("1".equals(name)) {
                        sexName.put("name", "男");
                        sexNames.add("男");
                    } else {
                        sexName.put("name", "女");
                        sexNames.add("女");
                    }
                }
                mbrMap.put("sexNames", sexNames);
            }

            //4. 创建日历对象
            Calendar calendar = Calendar.getInstance();
            //获取当前的年份
            int yearNow = calendar.get(Calendar.YEAR);
            //获取当前的月份
            int monthNow = calendar.get(Calendar.MONTH);
            //获取当前的日
            int dayNow = calendar.get(Calendar.DAY_OF_MONTH);

            //5. 自定义年龄段对象 ：0-18 、18-30、30-45、45以上
            List<String> ageGroups = new ArrayList<>();
            ageGroups.add("0-18岁");
            ageGroups.add("18-30岁");
            ageGroups.add("30-45岁");
            ageGroups.add("45岁以上");
            //年龄段对应的会员数量
            int count1 = 0;
            int count2 = 0;
            int count3 = 0;
            int count4 = 0;


            //将年龄段添加到集合中
            mbrMap.put("name", ageGroups);


            //存放年龄段 和对应的会员数量
            List<Map> ageMap = new ArrayList<>();
            Map map = new HashMap();

            //6. 查询所有会员的出生日期
            List<String> brithdayList = memberDao.findMemberBrithday();

            if (brithdayList != null && brithdayList.size() > 0) {
                for (String brithday : brithdayList) {
                    //将所有会员的String类型的出生日期转成Date格式
                    Date brithdayDate = null;
                    try {
                        brithdayDate = DateUtils.parseString2Date(brithday);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //获取年月日
                    int yearBirth = brithdayDate.getYear() + 1900;
                    int monthBirth = brithdayDate.getMonth();
                    int dayBirth = brithdayDate.getDay();
                    int age = yearNow - yearBirth;
                    //如果当前月份小于出生月份，或者当前月份等于出生月份但是当前日期小于出生日期，年龄-1
                    if (monthNow < monthBirth || (monthNow == monthBirth && dayNow < dayBirth)) {
                        age = age - 1;
                    }
                    if (age >= 0 && age < 18) {
                        //int count1 = memberDao.findMemberNumberByBrithday(brithday);
                        count1++;
                    }
                    if (age >= 18 && age < 30) {
                        count2++;
                    }
                    if (age > 30 && age < 45) {
                        count3++;
                    }
                    if (age > 45) {
                        count4++;
                    }
                }
                //将年龄段添加到map中，将key修改成name
                map.put("name", ageGroups);
                //创建List<Integer>，用于存放会员数
                List<Integer> ageList = new ArrayList<>();
                ageList.add(count1);
                ageList.add(count2);
                ageList.add(count3);
                ageList.add(count4);
                map.put("value", ageList);

                //
                List<Map> resultList = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    Map map1 = new HashMap();
                    map1.put("name", ageGroups.get(i));
                    map1.put("value", ageList.get(i));
                    resultList.add(map1);
                }
                //
                ageMap.add(map);
                mbrMap.put("ageMap", resultList);
            }

            return mbrMap;
        }
    }

