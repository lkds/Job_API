package com.pilot.job.controller;

import java.math.BigDecimal;
import java.util.*;

import com.pilot.job.entity.City;
import com.pilot.job.entity.Company;
import com.pilot.job.entity.Job;
import com.pilot.job.entity.Result;
import com.pilot.job.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    String color[] = { "#00a8cc", "#0c7b93", "#27496d", "#8566aa", "#6983aa", "#8ec6c5", "#f4ebc1", "#a0c1b8",
            "#709fb0", "#726a95", "#436f8a", "#438a5e", "#bac964", "#f7fbe1", "#dae1e7", "#00909e", "#27496d",
            "#142850" };
    int currColor = 0;

    public List<Map<String, Object>> reBuild(List<Job> allJob, String father) {
        List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
        for (Job j : allJob) {
            Map<String, Object> m = new HashMap<>();
            if (j.getJtypeFather().equals(father)) {
                m.put("name", j.getJtypeNow());
                m.put("itemStyle", new HashMap<String, Object>() {
                    {
                        put("color", color[currColor]);
                    }
                });
                currColor = (currColor + 1) % color.length;
                m.put("value", j.getCount());
                List<Map<String, Object>> res = reBuild(allJob, j.getJtypeNow());
                if (res.size() > 0) {
                    m.put("children", res);
                }
                l.add(m);
            }
        }
        return l;
    }

    /**
     * 不同岗位薪资
     * 
     * @return
     */
    @RequestMapping("/jobAmounts")
    public Result getJobAmounts() {
        Result res = new Result();
        List<Map<String, Object>> body = new ArrayList<Map<String, Object>>();
        List<Job> allJobs = jm.getJobAmounts();
        try {
            body = this.reBuild(allJobs, "所有");
            res.setMsg("success");
            res.setStatus(1);
        } catch (Exception e) {
            res.setMsg(e.toString());
        }
        res.setBody(body);
        return res;
    }

    /**
     * 不同行业平均薪资
     * 
     * @return
     */
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
                jobAvgSalary.add(new BigDecimal(j.getJavSalary() * 1000).setScale(2, 1).doubleValue());
                jobMinSalary.add(new BigDecimal(j.getJavminSalary() * 1000).setScale(2, 1).doubleValue());
                jobMaxSalary.add(new BigDecimal(j.getJavmaxSalary() * 1000).setScale(2, 1).doubleValue());
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

    /**
     * 不同岗位学历
     */
    @RequestMapping("/jobEducation/{industry}")
    public Result getJobEducation(@PathVariable("industry") String industry) {
        Result res = new Result();
        List<Map<String, Object>> body = new ArrayList<Map<String, Object>>() {
            {
                add(new HashMap<String, Object>());
                add(new HashMap<String, Object>());
                add(new HashMap<String, Object>());
                add(new HashMap<String, Object>());
            }
        };
        try {

            List<Job> allJob = jm.getJobEducation(industry);
            for (Job j : allJob) {
                String edu = j.getJeducation();
                try {

                    switch (edu) {
                        case "高中":
                            body.set(0, new HashMap<String, Object>() {
                                {
                                    put("post", j.getJeducation());
                                    put("number", j.getCount());
                                }
                            });
                            break;
                        case "大专":
                            body.set(1, new HashMap<String, Object>() {
                                {
                                    put("post", j.getJeducation());
                                    put("number", j.getCount());
                                }
                            });
                            break;
                        case "本科":
                            body.set(2, new HashMap<String, Object>() {
                                {
                                    put("post", j.getJeducation());
                                    put("number", j.getCount());
                                }
                            });
                            break;
                        case "硕士":
                            body.set(3, new HashMap<String, Object>() {
                                {
                                    put("post", j.getJeducation());
                                    put("number", j.getCount());
                                }
                            });
                            break;
                        default:
                    }
                } catch (Exception e) {
                    continue;
                }

            }
            res.setStatus(1);
            res.setMsg("success");
            res.setBody(body);
        } catch (Exception e) {
            res.setMsg(e.toString());
        }

        return res;
    }

    /**
     * 不同学历平均薪资
     */
    @RequestMapping("/educationSalary/{industry1}/{industry2}/{industry3}/{industry4}")
    public Result getEducationSalary(@PathVariable String industry1, @PathVariable String industry2,
            @PathVariable String industry3, @PathVariable String industry4) {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>();
        //
        List<String> education = Arrays.asList("初中", "高中", "大专", "本科", "硕士");
        List<String> industry = Arrays.asList(industry1, industry2, industry3, industry4);
        Double[][] avgSalary = new Double[4][5];
        try {
            List<Job> jobArr = jm.getEducationSalary(industry1, industry2, industry3, industry4);
            int x, y;
            for (Job j : jobArr) {
                x = industry.indexOf(j.getJindustry());
                y = education.indexOf(j.getJeducation());
                if (x != -1 && y != -1) {
                    avgSalary[x][y] = new BigDecimal(j.getJavSalary() * 1000).setScale(2, 1).doubleValue();
                }
            }
            body.put("education", education);
            body.put("industry", industry);
            body.put("avgSalary", avgSalary);
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

    /**
     * 薪资-学历-经验
     * 
     * @return
     */
    @RequestMapping("/expEduSalary")
    public Result getExpEduSalary() {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>();
        List education = Arrays.asList("初中", "高中", "大专", "本科", "硕士");
        List experience = Arrays.asList("不限", "1", "1-3", "3-5", "5-10");
        ArrayList<ArrayList<Object>> salary = new ArrayList<ArrayList<Object>>();
        try {
            ArrayList<Job> jobArr = (ArrayList<Job>) jm.getExpEduSalary();
            int x, y, index;
            for (int i = 0; i < education.size(); i++) {
                for (int j = 0; j < experience.size(); j++) {
                    ArrayList<Object> a = new ArrayList<Object>();
                    a.add(i);
                    a.add(j);
                    a.add(0);
                    salary.add(a);
                }
            }
            String exp;
            for (Job j : jobArr) {
                if (j.getJexperience() == null)
                    exp = "不限";
                else {
                    exp = j.getJexperience();
                    if (exp.equals("0"))
                        exp = "不限";
                    else if (exp.equals("1"))
                        exp = "1";
                    else if (exp.equals("1-2") || exp.equals("1-3") || exp.equals("2"))
                        exp = "1-3";
                    else if (exp.equals("3-4") || exp.equals("3-5"))
                        exp = "3-5";
                    else if (exp.equals("5-10") || exp.equals("5-7") || exp.equals("6-7") || exp.equals("8-9")
                            || exp.equals("8-10"))
                        exp = "5-10";

                }
                x = education.indexOf(j.getJeducation());
                y = experience.indexOf(exp);
                if (x != -1 && y != -1) {
                    index = x * experience.size() + y;
                    ArrayList<Object> a = new ArrayList<Object>();
                    a = salary.get(index);
                    a.set(2, new BigDecimal(j.getJavSalary() * 1000).setScale(2, 1).doubleValue());
                    salary.set(index, a);
                }
            }
            body.put("education", education);
            body.put("experience", experience);
            body.put("salary", salary);
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

    /**
     * 不同规模企业薪资
     * 
     * @return
     */
    @RequestMapping("/salaryOfScale")
    public Result getSalaryOfScale() {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>(10);
        ArrayList<String> comSize = new ArrayList<>();
        ArrayList<Double> ratio = new ArrayList<>();
        ArrayList<Double> JavSalary = new ArrayList<>();
        ArrayList<Integer> count = new ArrayList<>();
        ArrayList<Double> salary = new ArrayList<>();
        try {
            List<Company> allCom = jm.getSalaryOfScale();
            // 排序
            Collections.sort(allCom);
            for (Company c : allCom) {
                comSize.add(c.getJcomSize());
                ratio.add(c.getRadio());
                count.add(c.getCount());
                JavSalary.add(new BigDecimal(c.getJavSalary() * 1000).setScale(2, 1).doubleValue());
            }
            body.put("scale", comSize);
            body.put("ratio", ratio);
            body.put("count", count);
            body.put("JavSalary", JavSalary);
            res.setBody(body);
            res.setStatus(1);
            res.setMsg("success");
        } catch (Exception e) {
            res.setMsg(e.toString());
        }
        return res;
    }

    /**
     * 不同性质企业
     */
    @RequestMapping("/salaryOfProp")
    public Result getSalaryOfProp() {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>(10);

        ArrayList<String> comType = new ArrayList<>();
        ArrayList<Double> ratio = new ArrayList<>();
        ArrayList<Integer> count = new ArrayList<>();
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        try {
            List<Company> allCom = jm.getSalaryOfProp();
            for (Company c : allCom) {
                comType.add(c.getJcomFinanceStage());
                // ratio.add(c.getRadio());
                count.add(c.getCount());
                Map<String, Object> map = new HashMap<>();
                map.put("name", c.getJcomFinanceStage());
                map.put("value", c.getCount());
                data.add(map);
            }
            body.put("companyType", comType);
            // body.put("ration", ratio);
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

    /**
     * 不同地区平均薪资
     * 
     * @return
     */
    @RequestMapping("/areaSalary")
    public Result getAreaSalary() {
        Result res = new Result();
        List<Map<String, Object>> body = new ArrayList<>();
        try {
            List<City> allCom = jm.getAreaSalary();
            for (City c : allCom) {

                Map<String, Object> map = new HashMap<>(2);
                map.put("name", c.getJcity());
                map.put("value", new BigDecimal(c.getJavSalary() * 1000).setScale(2, 1).doubleValue());
                body.add(map);
            }
            res.setBody(body);
            res.setStatus(1);
            res.setMsg("success");
        } catch (Exception e) {
            res.setBody(body);
            res.setMsg(e.toString());
        }
        return res;
    }

    /**
     * 比较两个地区的行业分布
     * 
     * @param province1
     * @param province2
     * @return
     */
    @RequestMapping("/twoProvinceCount/{province1}/{province2}")
    public Result getTwoProvinceCount(@PathVariable String province1, @PathVariable String province2) {
        Result res = new Result();
        Map<String, Object> body = new HashMap<>();
        ArrayList<String> titleData = new ArrayList<String>();
        titleData.add(province1);
        titleData.add(province2);
        ArrayList<Map<String, Object>> typeStander = new ArrayList<Map<String, Object>>();
        ArrayList<Map<String, Object>> typeData = new ArrayList<Map<String, Object>>();
        try {
            List<Job> countArr = jm.getTwoProvinceCount(province1, province2);
            ArrayList<String> industries = new ArrayList<String>();
            for (Job j : countArr) {
                if (!industries.contains(j.getJindustry()))
                    industries.add(j.getJindustry());
            }
            for (String s : industries) {
                Map<String, Object> industry = new HashMap<String, Object>();
                industry.put("name", s);
                industry.put("max", 6000);
                typeStander.add(industry);
            }
            int[] value1 = new int[5];
            int[] value2 = new int[5];
            for (Job j : countArr) {
                if (j.getJprovince().equals(province1))
                    value1[industries.indexOf(j.getJindustry())] = j.getCount();
                else if (j.getJprovince().equals(province2))
                    value2[industries.indexOf(j.getJindustry())] = j.getCount();
            }
            Map<String, Object> v1 = new HashMap<String, Object>();
            Map<String, Object> v2 = new HashMap<String, Object>();
            v1.put("name", province1);
            v1.put("value", value1);
            v2.put("name", province2);
            v2.put("value", value2);
            typeData.add(v1);
            typeData.add(v2);
            body.put("titleData", titleData);
            body.put("typeStander", typeStander);
            body.put("typeData", typeData);
            res.setBody(body);
            res.setStatus(1);
            res.setMsg("success");
        } catch (Exception e) {
            res.setMsg(e.toString());
        }
        return res;
    }

    /**
     *
     * 不同地域行业分析
     *
     * @param province
     * @return
     */
    @RequestMapping("/areaJob/{province}")
    @GetMapping("/areaJob/{province}")
    public Result getAreaJob(@PathVariable("province") String province) {
        // TODO 去除其它
        Result res = new Result();
        Map<String, Object> body = new HashMap<>(350);
        try {
            List<Job> allJob = jm.getTopJob(province, 5);
            ArrayList<String> topJobs = new ArrayList<>(5);
            ArrayList<Integer> jobCount = new ArrayList<>(5);
            ArrayList<Double> jobRatio = new ArrayList<>(5);
            for (Job j : allJob) {
                topJobs.add(j.getJindustry());
                jobCount.add(j.getCount());
                jobRatio.add(new BigDecimal(j.getRatio() * 100).setScale(4, 1).doubleValue());

            }
            body.put("topJobs", topJobs);
            body.put("jobCount", jobCount);
            body.put("jobRatio", jobRatio);
            res.setBody(body);
            res.setStatus(1);
            res.setMsg("success");
        } catch (Exception e) {
            res.setMsg(e.toString());
        }
        return res;
    }

    /**
     * 不同行业需求人数分析
     */
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

    /**
     * 行业需求关键词
     * 
     * @param jobType
     * @return
     */
    @RequestMapping("/wordCloud/{jobType}")
    public Result getWordCloud(@PathVariable("jobType") String jobType) {
        Result res = new Result();

        List<Map<String, Object>> body = new ArrayList<>();
        try {

            List<Job> allWords = jm.getWordCloud(jobType, 200);
            for (Job j : allWords) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("name", j.getJname());
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

    /**
     * 获取最新职业信息
     * 
     * @return
     */
    @RequestMapping("/newJobs/{total}")
    public Result getNewJob(@PathVariable("total") int total) {
        Result res = new Result();
        List<Map<String, Object>> body = new ArrayList<Map<String, Object>>(total);
        try {
            List<Job> allJobs = jm.getNewJob(total);
            for (Job j : allJobs) {
                body.add(new HashMap<String, Object>(3) {
                    {
                        put("name", j.getJname());
                        put("company", j.getJcompany());
                        put("salary", new BigDecimal((j.getJminSalary() + j.getJavmaxSalary()) * 500).setScale(2, 1)
                                .doubleValue());
                    }
                });
            }
            res.setBody(body);
            res.setMsg("success");
            res.setStatus(1);
        } catch (Exception e) {
            res.setMsg(e.toString());
        }
        return res;
    }

    /**
     * 获取语言排行
     * 
     * @return
     */
    @RequestMapping("/languageRank")
    public Result getLanguageRank() {
        Result res = new Result();
        Map<String, Object> body = new HashMap<String, Object>(2);
        try {
            List<Job> allJobs = jm.getLanguageRank();

            List<String> language = new ArrayList<>(20);
            List<Double> salary = new ArrayList<>(20);
            for (Job j : allJobs) {
                language.add(j.getJname());
                salary.add(new BigDecimal(j.getJavSalary()).setScale(3, 1).doubleValue() * 1000);
            }
            Collections.reverse(language);
            Collections.reverse(salary);
            body.put("language", language);
            body.put("salary", salary);
        } catch (Exception e) {
            res.setMsg(e.toString());
        }
        res.setBody(body);
        res.setMsg("success");
        res.setStatus(1);
        return res;
    }

    /**
     * 获取互联网职位数量
     * 
     * @return 结果
     */
    @RequestMapping("/internetAmounts")
    public Result getInternetAmounts() {
        Result res = new Result();
        List<Job> allJobs = jm.getJobAmounts();
        Map<String, Object> body = new HashMap<String, Object>();
        List<Map<String, Object>> l1 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> l2 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> l3 = new ArrayList<Map<String, Object>>();
        for (Job j : allJobs) {
            if (j.getJtypeFather().equals("IT·互联网")) {
                l1.add(new HashMap<String, Object>() {
                    {
                        if (j.getJtypeNow() != null) {
                            put("name", j.getJtypeNow());
                            put("value", j.getCount());
                        }

                    }
                });
            }
        }
        int i = 0;
        for (; i < l1.size(); ++i) {
            boolean find = false;
            for (Job j : allJobs) {
                try {
                    if (j.getJtypeFather().equals(l1.get(i).get("name").toString())) {
                        find = true;
                        l2.add(new HashMap<String, Object>() {
                            {
                                put("name", j.getJtypeNow());
                                put("value", j.getCount());
                            }
                        });
                    }
                } catch (Exception e) {

                }
            }
            if (!find) {
                int finalI1 = i;
                l2.add(new HashMap<String, Object>() {
                    {
                        put("name", l1.get(finalI1).get("name"));
                        put("value", l1.get(finalI1).get("value"));
                    }
                });
            }
        }
        for (i = 0; i < l2.size(); ++i) {
            boolean find = false;
            for (Job j : allJobs) {
                try {
                    if (j.getJtypeFather().equals(l2.get(i).get("name").toString())) {
                        find = true;
                        l3.add(new HashMap<String, Object>() {
                            {
                                put("name", j.getJtypeNow());
                                put("value", j.getCount());
                            }
                        });
                    }
                } catch (Exception e) {

                }
            }
            if (!find) {
                int finalI = i;
                l3.add(new HashMap<String, Object>() {
                    {
                        put("name", l2.get(finalI).get("name"));
                        put("value", l2.get(finalI).get("value"));
                    }
                });
            }
        }
        body.put("first", l1);
        body.put("second", l2);
        body.put("third", l3);
        res.setBody(body);
        res.setMsg("success");
        res.setStatus(1);
        return res;
    }
}