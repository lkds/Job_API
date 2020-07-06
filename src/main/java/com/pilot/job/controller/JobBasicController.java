package com.pilot.job.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pilot.job.entity.City;
import com.pilot.job.entity.Company;
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
        Map<String, Object> body = new HashMap<>(10);
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
        // catch (RecoverableDataAccessException e){
        // res.setMsg("数据库访问失败！");
        // }finally {
        // res.setMsg("未知错误！");
        // }
        catch (Exception e) {
            res.setMsg(e.toString());
        }
        return res;
    }

    // @RequestMapping("/jobEducation/{industry}")
    // public Result getJobEducation(@PathVariable("industry") String industry) {
    // int index;
    // Result res = new Result();
    // Map<String, Object> body = new HashMap<>(2);
    // ArrayList<String> jobType = new ArrayList<String>();
    // ArrayList<Map<String, ArrayList<Double>>> proportionDemand = new
    // ArrayList<Map<String, ArrayList<Double>>>();
    // try {
    // List<Job> jobArr = jm.getJobEducation(industry);
    // for (Job j : jobArr) {
    // index = jobType.indexOf(j.getJindustry());
    // if (index == -1) {
    // Map<String, ArrayList<Double>> proDe = new HashMap<String,
    // ArrayList<Double>>();
    // ArrayList<Double> countAndRatio = new ArrayList<Double>();
    // countAndRatio.add(j.getCount());
    // countAndRatio.add(j.getRatio());
    // proDe.put(j.getJeducation(), countAndRatio);
    // jobType.add(j.getJindustry());
    // proportionDemand.add(proDe);
    // } else {
    // ArrayList<Double> countAndRatio = new ArrayList<Double>();
    // Map<String, ArrayList<Double>> proDe = proportionDemand.get(index);
    // countAndRatio.add(j.getCount());
    // countAndRatio.add(j.getRatio());
    // proDe.put(j.getJeducation(), countAndRatio);
    // proportionDemand.set(index, proDe);
    // }
    // }
    // body.put("education", proportionDemand);
    // res.setBody(body);
    // res.setStatus(1);
    // res.setMsg("success");
    // }
    // // catch (RecoverableDataAccessException e){
    // // res.setMsg("数据库访问失败！");
    // // }finally {
    // // res.setMsg("未知错误！");
    // // }
    // catch (Exception e) {
    // res.setMsg(e.toString());
    // }

    // return res;
    // }

    @RequestMapping("/jobEducation/{industry}")
    public Result getJobEducation(@PathVariable("industry") String industry) {
        Result res = new Result();
        List<Map<String, Object>> body = new ArrayList<Map<String, Object>>();
        List<Job> allJob = jm.getJobEducation(industry);
        for (Job j : allJob) {
            body.add(new HashMap<String, Object>() {
                {
                    put("post", j.getJeducation());
                    put("number", j.getCount());
                }
            });
        }
        res.setBody(body);
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
        Result res = new Result();
        Map<String, Object> body = new HashMap<>(10);
        ArrayList<String> comSize = new ArrayList<>();
        ArrayList<Double> ration = new ArrayList<>();
        ArrayList<Integer> count = new ArrayList<>();
        try {
            List<Company> allCom = jm.getSalaryOfScale();
            for (Company c : allCom) {
                comSize.add(c.getJcomSize());
                ration.add(c.getRadio());
                count.add(c.getCount());
            }
            body.put("scale", comSize);
            body.put("ration", ration);
            body.put("count", count);
            res.setBody(body);
            res.setStatus(1);
            res.setMsg("success");
        } catch (Exception e) {
            res.setMsg(e.toString());
        }
        return res;
    }

    @RequestMapping("/salaryOfProp")
    public Result getSalaryOfProp() {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>(10);
        ArrayList<String> comType = new ArrayList<>();
        ArrayList<Double> ration = new ArrayList<>();
        ArrayList<Integer> count = new ArrayList<>();
        try {
            List<Company> allCom = jm.getSalaryOfProp();
            for (Company c : allCom) {
                comType.add(c.getJcomType());
                ration.add(c.getRadio());
                count.add(c.getCount());
            }
            body.put("scale", comType);
            body.put("ration", ration);
            body.put("count", count);
            res.setBody(body);
            res.setStatus(1);
            res.setMsg("success");
        } catch (Exception e) {
            res.setMsg(e.toString());
        }
        return res;
    }

    @RequestMapping("/areaSalary")
    public Result getAreaSalary() {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>(350);
        try {
            List<City> allCom = jm.getAreaSalary();
            for (City c : allCom) {
                body.put(c.getJcity(), c.getJavSalary());
            }
            res.setBody(body);
            res.setStatus(1);
            res.setMsg("success");
        } catch (Exception e) {
            res.setMsg(e.toString());
        }
        return res;
    }

    @RequestMapping("/areaJob/{province}")
    public Result getAreaJob(@PathVariable("province") String province) {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>(350);
        try {
            List<Job> allJob = jm.getTopJob(province, 5);
            ArrayList<String> topJobs = new ArrayList<>(5);
            ArrayList<Integer> jobCount = new ArrayList<>(5);
            for (Job j : allJob) {
                topJobs.add(j.getJindustry());
                jobCount.add(j.getCount());
            }
            body.put("topJobs", topJobs);
            body.put("jobCount", jobCount);
            res.setBody(body);
            res.setStatus(1);
            res.setMsg("success");
        } catch (Exception e) {
            res.setMsg(e.toString());
        }
        return res;
    }

    @RequestMapping("/jobCount")
    public Result getJobCount() {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>();
        ArrayList<String> jobType = new ArrayList<String>();
        ArrayList<Integer> hireCount = new ArrayList<Integer>();
        try {
            ArrayList<Job> jobArr = (ArrayList<Job>) jm.getAverageSalary();
            for (Job j : jobArr) {
                jobType.add(j.getJindustry());
                hireCount.add(j.getTotalhirecount());
            }
            body.put("jobType", jobType);
            body.put("hireCount", hireCount);
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

    @RequestMapping("/wordCloud/{jobType}")
    public Result getWordCloud(@PathVariable("jobType") String jobType) {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>(500);
        try {
            List<Job> allWords = jm.getWordCloud(jobType);
            for (Job j : allWords) {
                body.put(j.getJrequirements(), j.getCount());
            }
            res.setBody(body);
            res.setStatus(1);
            res.setMsg("success");
        } catch (Exception e) {
            res.setMsg(e.toString());
        }
        return res;
    }

}