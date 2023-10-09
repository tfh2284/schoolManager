package com.xxxx.school.dao;

import com.xxxx.school.base.BaseMapper;
import com.xxxx.school.vo.Course;

public interface CourseMapper extends BaseMapper<Course,Integer> {

    // 通过用户名查询用户记录，返回用户对象
    public Course queryCourseByName(String courseName);

}