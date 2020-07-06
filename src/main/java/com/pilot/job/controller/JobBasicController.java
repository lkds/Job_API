package com.pilot.job.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pilot.job.entity.Job;
import com.pilot.job.entity.Result;
import com.pilot.job.mapper.JobMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fnzs
 */
@RestController
@RequestMapping("/job")
public class JobBasicController {
    @Autowired
    private JobMapper jm;

    @RequestMapping("/jobAmounts")
    public Result getJobAmounts() {
        return new Result();
    }

    @RequestMapping("/averageSalary")
    public Result getAverageSalary() {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>(2);
        ArrayList<String> jobType = new ArrayList<String>();
        ArrayList<Double> jobAvgSalary = new ArrayList<Double>();
        ArrayList<Double> jobMinSalary = new ArrayList<Double>();
        ArrayList<Double> jobMaxSalary = new ArrayList<Double>();
        try {
            ArrayList<Job> jobArr = (ArrayList<Job>) jm.getAverageSalary();
            for (Job j : jobArr) {
                jobType.add(j.getJindustry());
                jobAvgSalary.add(j.getJavSalary());
                jobMinSalary.add(j.getJavminSalary());
                jobMaxSalary.add(j.getJavmaxSalary());
            }
            body.put("jobType", jobType);
            body.put("avgSalary", jobAvgSalary);
            body.put("maxSalary", jobMaxSalary);
            body.put("minSalary", jobMinSalary);
            res.setBody(body);
            res.setStatus(1);
            res.setMsg("success");
        }
//        catch (RecoverableDataAccessException e){
//            res.setMsg("数据库访问失败！");
//        }finally {
//            res.setMsg("未知错误！");
//        }
        catch(Exception e){
               res.setMsg(e.toString());
        }
        return res;
    }

    @RequestMapping("/jobEducation")
    public Result getJobEducation() {
        int index;
        Result res = new Result();
        Map<String, Object> body = new HashMap<>(2);
        ArrayList<String> jobType = new ArrayList<String>();
        ArrayList<Map<String,ArrayList<Double>>> proportionDemand = new ArrayList<Map<String,ArrayList<Double>>>();
        try {
            ArrayList<Job> jobArr = (ArrayList<Job>) jm.getJobEducation();
            for (Job j : jobArr) {
                index=jobType.indexOf(j.getJindustry());
                if(index==-1)
                {
                    Map<String,ArrayList<Double>> proDe = new HashMap<String,ArrayList<Double>>();
                    ArrayList<Double> countAndRatio = new ArrayList<Double>();
                    countAndRatio.add(j.getCount());
                    countAndRatio.add(j.getRatio());
                    proDe.put(j.getJeducation(), countAndRatio);
                    jobType.add(j.getJindustry());
                    proportionDemand.add(proDe);
                }
                else
                {
                    ArrayList<Double> countAndRatio = new ArrayList<Double>();
                    Map<String,ArrayList<Double>> proDe = proportionDemand.get(index);
                    countAndRatio.add(j.getCount());
                    countAndRatio.add(j.getRatio());
                    proDe.put(j.getJeducation() ,countAndRatio);
                    proportionDemand.set(index, proDe);
                }
            }
            body.put("jobType", jobType);
            body.put("education", proportionDemand);
            res.setBody(body);
            res.setStatus(1);
            res.setMsg("success");
        }
//        catch (RecoverableDataAccessException e){
//            res.setMsg("数据库访问失败！");
//        }finally {
//            res.setMsg("未知错误！");
//        }
        catch(Exception e){
            res.setMsg(e.toString());
        }

        return res;
    }

    @RequestMapping("/educationSalary")
    public Result getEducationSalary() {
        return new Result();
    }

    @RequestMapping("/expEduSalary")
    public Result getExpEduSalary() {
        return new Result();
    }

    @RequestMapping("/salaryOfScale")
    public Result getSalaryOfScale() {
        return new Result();
    }

    @RequestMapping("/salaryOfProp")
    public Result getSalaryOfProp() {
        return new Result();
    }

    @RequestMapping("/areaSalary")
    public Result getAreaSalary() {
        return new Result();
    }

    @RequestMapping("/areaJob")
    public Result getAreaJob() {
        return new Result();
    }

    @RequestMapping("/jobCount")
    public Result getJobCount() {
        return new Result();
    }

    @RequestMapping("/wordCloud")
    public Result getWordCloud() {
        return new Result();
    }

}