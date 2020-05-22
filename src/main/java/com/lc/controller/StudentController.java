package com.lc.controller;

import com.lc.entity.Student;
import com.lc.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: lc
 * @CreateDate: 2020/05/03 10:24
 **/
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping("/insert")
    int insert(@RequestBody Student record){
        return studentService.insert(record);
    }

    @DeleteMapping("/delete/{id}")
    int deleteByPrimaryKey(@PathVariable("id") Integer id){
        return studentService.deleteByPrimaryKey(id);
    }

    @GetMapping("/get/{id}")
    Student selectByPrimaryKey(@PathVariable("id") Integer id){
        return studentService.selectByPrimaryKey(id);
    }

    @PostMapping("/update")
    int updateByPrimaryKey(@RequestBody Student record){
        return studentService.updateByPrimaryKey(record);
    }

}
