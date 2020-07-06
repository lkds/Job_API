package com.pilot.job.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 飞鸟
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Result {
    int status = 0;
    String msg;
    // private Map<String, Object> body = new HashMap<String, Object>();
    Object body;
}