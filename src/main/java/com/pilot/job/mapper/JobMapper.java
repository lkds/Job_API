package com.pilot.job.mapper;

import java.util.List;

import com.pilot.job.entity.City;
import com.pilot.job.entity.Company;
import com.pilot.job.entity.Job;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author fnzs
 */
@Mapper
@Component
public interface JobMapper {
    /**
     * 获取不同行业的平均薪资
     * 
     * @return 薪资数组
     */
    @Select("SELECT Jindustry,JavSalary,JavmaxSalary,JavminSalary FROM Demand1_2")
    List<Job> getAverageSalary();

    /**
     * 2-1 从数据库获取不同规模的
     * 
     * @return 公司列表
     */
    @Select("SELECT JcomSize,count,Radio,JavSalary FROM Demand2_1")
    List<Company> getSalaryOfScale();

    /**
     * 2-2 从数据库获取不同性质的
     * 
     * @return 公司列表
     */
    @Select("SELECT JcomType,count,Radio,JavSalary FROM Demand2_2")
    List<Company> getSalaryOfProp();

    /**
     * 3-1获取每个城市的平均薪资
     * 
     * @return 城市列表
     */
    @Select("SELECT Jcity,JavSalary FROM Demand3_1")
    List<City> getAreaSalary();

    /**
     * 获取每个地区的top行业
     * 
     * @param city
     * @return
     */
    @Select("SELECT Jindustry,Count,Ratio FROM Demand3_2 WHERE Jprovince = #{province} ORDER BY `Count` DESC LIMIT #{t}")
    List<Job> getTopJob(String province, int t);

    /**
     * 5-1 获取词云分布
     * 
     * @param jobType 职业类型
     * @return Job列表
     */
    @Select("SELECT Jrequirements,count FROM Demand5_1 WHERE Jindustry = #{jobType}")
    List<Job> getWordCloud(String jobType);

    /**
     * 获取不同岗位的学历要求
     *
     * @return 包含学历字典的数组
     */
    @Select("SELECT Jindustry,Jeducation,count,Ratio FROM Demand1_3 WHERE Jindustry=#{industry}")
    List<Job> getJobEducation(String industry);

    /**
     * 获取不同行业需求的人数
     *
     * @return 两个字典，第一个为行业，第二个为对应的需求人数
     */
    @Select("SELECT Jindustry,Totalhirecount FROM Demand4_1 ORDER BY Totalhirecount DESC LIMIT 0,10")
    List<Job> getJobCount();

    /**
     * 经验-学历-薪资关系
     *
     * @return 三个字典
     */
    @Select("SELECT Jexperience,Jeducation,JavSalary FROM Demand1_5")
    List<Job> getExpEduSalary();
}