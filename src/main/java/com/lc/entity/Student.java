package com.lc.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * student
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student implements Serializable {
    private Integer id;

    private String name;

    private String sex;

    private Date birthday;

    private Integer score;

    private static final long serialVersionUID = 1L;
}