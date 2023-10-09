package com.xxxx.school.controller;

import com.xxxx.school.base.BaseController;
import com.xxxx.school.base.ResultInfo;
import com.xxxx.school.query.CourseQuery;
import com.xxxx.school.service.CourseService;
import com.xxxx.school.vo.Course;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("user")
public class CourseController extends BaseController {

    @Resource
    private CourseService courseService;

    /**
     * 进入主页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    /**
     * 查询课程
     * @param query
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCourseByParams(CourseQuery query) {
        return courseService.queryCourseByParams(query);
    }

    /**
     * 添加课程
     * @param course
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addCourse(Course course){
        courseService.addCourse(course);
        return success("课程添加成功！");
    }

    /**
     * 更新课程
     * @param course
     * @return com.xxxx.crm.base.ResultInfo
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(Course course){
        courseService.updateCourse(course);
        return success("课程更新成功！");
    }

    /**
     * 打开添加或修改课程的页面
     */
    @RequestMapping("toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer courseId, HttpServletRequest request) {

        // 判断id是否为空，不为空表示更新操作，查询用户对象
        if (courseId != null) {
            // 通过id查询用户对象
            Course course = courseService.selectByPrimaryKey(courseId);
            // 将数据设置到请求域中
            request.setAttribute("userInfo",course);
        }

        return "user/add_update";
    }

    /**
     * 课程删除
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        courseService.deleteByIds(ids);
        return success("课程删除成功！");
    }
}
