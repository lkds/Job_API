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
public class City {
    private String Jcity;
    private Double JavSalary;

}