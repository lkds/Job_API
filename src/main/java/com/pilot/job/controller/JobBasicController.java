package com.pilot.job.controller;

import com.pilot.job.entity.Result;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fnzs
 */
@RestController
@RequestMapping("/job")
public class JobBasicController {
    @RequestMapping("/jobAmounts")
    public Result getJobAmounts() {
        return new Result();
    }

    @RequestMapping("/averageSalary")
    public Result getAverageSalary() {
        return new Result();
    }

    @RequestMapping("/jobEducation")
    public Result getJobEducation() {
        return new Result();
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