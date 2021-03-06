package com.pilot.job.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fnzs
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Job {
    private String Jname;
    private String Jindustry;
    private String Jtype;
    private String JcomSize;
    private double JavminSalary;
    private double JavSalary;
    private double JavmaxSalary;
    private String Jrequirements;
    private int count;
    private double Ratio;
    private String Jeducation;
    private int Totalhirecount;
    private String Jexperience;
    private double JmaxSalary;
    private double JminSalary;
    private String Jcompany;
    private String Jprovince;
    private String JtypeFather;
    private String JtypeNow;
}