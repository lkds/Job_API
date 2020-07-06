package com.pilot.job.mapper;

import java.util.List;

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
}