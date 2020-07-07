package com.pilot.job.controller;

import java.util.*;

import com.pilot.job.entity.City;
import com.pilot.job.entity.Company;
import com.pilot.job.entity.Job;
import com.pilot.job.entity.Result;
import com.pilot.job.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.web.bind.annotation.*;

/**
 * @author fnzs
 */
@CrossOrigin
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
                jobAvgSalary.add(j.getJavSalary()*1000);
                jobMinSalary.add(j.getJavminSalary()*1000);
                jobMaxSalary.add(j.getJavmaxSalary()*1000);
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

    @RequestMapping("/educationSalary/{industry1}/{industry2}/{industry3}/{industry4}")
    public Result getEducationSalary(@PathVariable String industry1, @PathVariable String industry2, @PathVariable String industry3, @PathVariable String industry4) {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>();
        List<String> education = Arrays.asList("初中","高中","大专","本科","硕士","博士");
        List<String> industry = Arrays.asList(industry1,industry2,industry3,industry4);
        Double [][] avgSalary = new Double[4][6];
        try {
            List<Job> jobArr = jm.getEducationSalary(industry1,industry2,industry3,industry4);
            int x,y;
            for (Job j : jobArr)
            {
                x=industry.indexOf(j.getJindustry());
                y=education.indexOf(j.getJeducation());
                if(x!=-1&&y!=-1) {
                    avgSalary[x][y]=j.getJavSalary()*1000;
                }
            }
            body.put("education", education);
            body.put("industry", industry);
            body.put("avgSalary", avgSalary);
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

    @RequestMapping("/expEduSalary")
    public Result getExpEduSalary() {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>();
        List education = Arrays.asList("初中","高中","大专","本科","硕士","博士");
        List experience = Arrays.asList("0","1","1-3","3-5","8-9","5-10");
        ArrayList<ArrayList<Object>> salary = new ArrayList<ArrayList<Object>>();
        for(int i=0;i<6;i++)
        {
            for(int j=0;j<6;j++)
            {
                ArrayList<Object> a = new ArrayList<Object>();
                a.add(i);
                a.add(j);
                a.add(0);
                salary.add(a);
            }
        }
        try {
            ArrayList<Job> jobArr = (ArrayList<Job>) jm.getExpEduSalary();
            int x,y,index;
            for (Job j : jobArr)
            {
                x=education.indexOf(j.getJeducation());
                y=experience.indexOf(j.getJexperience());
                if(x!=-1&&y!=-1)
                {
                    index=x*6+y;
                    ArrayList<Object> a = new ArrayList<Object>();
                    a=salary.get(index);
                    a.set(2,j.getJavSalary()*1000);
                    salary.set(index,a);
                }
            }
            body.put("education", education);
            body.put("experience", experience);
            body.put("salary", salary);
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

    @RequestMapping("/salaryOfScale")
    public Result getSalaryOfScale() {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>(10);
        ArrayList<String> comSize = new ArrayList<>();
        ArrayList<Double> ratio = new ArrayList<>();
        ArrayList<Double> JavSalary= new ArrayList<>();
        ArrayList<Integer> count = new ArrayList<>();
        try {
            List<Company> allCom = jm.getSalaryOfScale();
            //排序
            Collections.sort(allCom);
            for (Company c : allCom) {
                comSize.add(c.getJcomSize());
                ratio.add(c.getRadio());
                count.add(c.getCount());
                JavSalary.add(c.getJavSalary()*1000);
            }
            body.put("scale", comSize);
            body.put("ratio", ratio);
            body.put("count", count);
            body.put("JavSalary",JavSalary);
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
        ArrayList<Double> ratio = new ArrayList<>();
        ArrayList<Integer> count = new ArrayList<>();
        ArrayList<Map<String,Object>> data = new ArrayList<>();
        try {
            List<Company> allCom = jm.getSalaryOfProp();
            for (Company c : allCom) {
                comType.add(c.getJcomType());
                ratio.add(c.getRadio());
                count.add(c.getCount());
                Map<String,Object> map = new HashMap<>();
                map.put("name",c.getJcomType());
                map.put("value",c.getCount());
                data.add(map);
            }
            body.put("companyType", comType);
            body.put("ration", ratio);
            body.put("count", count);
            body.put("data", data);
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
        List<Map<String, Object>> body = new ArrayList<>();
        try {
            List<City> allCom = jm.getAreaSalary();
            for (City c : allCom) {

                Map<String, Object> map = new HashMap<>(2);
                map.put("name", c.getJcity());
                map.put("value", c.getJavSalary()*1000);
                body.add(map);
            }
            res.setBody(body);
            res.setStatus(1);
            res.setMsg("success");
        } catch (Exception e) {
            res.setMsg(e.toString());
        }
        return res;
    }

    @RequestMapping("/twoProvinceCount/{province1}/{province2}")
    public Result getTwoProvinceCount(@PathVariable String province1, @PathVariable String province2) {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>();
        ArrayList<String> titleData = new ArrayList<String>();
        titleData.add(province1);
        titleData.add(province2);
        ArrayList<Map<String,Object>> typeStander = new ArrayList<Map<String,Object>>();
        ArrayList<Map<String,Object>> typeData = new ArrayList<Map<String,Object>>();
        try {
            List<Job> countArr = jm.getTwoProvinceCount(province1,province2);
            ArrayList<String> industries = new ArrayList<String>();
            for (Job j : countArr) {
                if(!industries.contains(j.getJindustry()))
                    industries.add(j.getJindustry());
            }
            for (String s : industries){
                Map<String,Object> industry = new HashMap<String, Object>();
                industry.put("name",s);
                industry.put("max",200000);
                typeStander.add(industry);
            }
            int [] value1 = new int[5];
            int [] value2 = new int[5];
            for (Job j : countArr) {
                if(j.getJprovince().equals(province1))
                    value1[industries.indexOf(j.getJindustry())]=j.getCount();
                else if(j.getJprovince().equals(province2))
                    value2[industries.indexOf(j.getJindustry())]=j.getCount();
            }
            Map<String,Object> v1 = new HashMap<String, Object>();
            Map<String,Object> v2 = new HashMap<String, Object>();
            v1.put("name",province1);
            v1.put("value",value1);
            v2.put("name",province2);
            v2.put("value",value2);
            typeData.add(v1);
            typeData.add(v2);
            body.put("titleData",titleData);
            body.put("typeStander",typeStander);
            body.put("typeData",typeData);
            res.setBody(body);
            res.setStatus(1);
            res.setMsg("success");
        } catch (Exception e) {
            res.setMsg(e.toString());
        }
        return res;
    }

    @RequestMapping("/areaJob/{province}")
    @GetMapping("/areaJob/{province}")
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
            ArrayList<Job> jobArr = (ArrayList<Job>) jm.getJobCount();
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

        List< Map<String, Object>> body = new ArrayList<>();
        try {

            List<Job> allWords = jm.getWordCloud(jobType);
            for (Job j : allWords) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("name", j.getJrequirements());
                map.put("value", j.getCount());
                body.add(map);
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