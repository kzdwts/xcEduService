package com.xuecheng.test.freemarker.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 测试类 学生
 */
@Data
@ToString
public class Student implements Serializable {

    private String name;
    private Integer age;
    private Date birthday;
    private Float money;
    private List<Student> friends;
    private Student bestFriend;
}
