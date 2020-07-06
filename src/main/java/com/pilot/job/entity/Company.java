package com.pilot.job.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {
    private String Jcompany;
    private String JcomSize;
    private int count;
    private double Radio;
    private double JavSalary;
    private String JcomType;
}
