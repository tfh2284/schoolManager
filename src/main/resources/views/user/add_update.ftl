<!DOCTYPE html>
<html>
    <head>
        <#include "../common.ftl">
    </head>
    <body class="childrenBody">
        <form class="layui-form" style="width:80%;">
            <#-- 用户ID -->
            <input name="id" type="hidden" value="${(userInfo.courseId)!}"/>  <#-- 跟后台的CourseContoller 65行 -->
            <div class="layui-form-item layui-row layui-col-xs12">
                <label class="layui-form-label">课程名称</label>
                <div class="layui-input-block">
                    <input type="text" class="layui-input courseName"
                           lay-verify="required" name="courseName" id="courseName"  value="${(userInfo.courseName)!}" placeholder="请输入课程名称">
                </div>
            </div>
            <div class="layui-form-item layui-row layui-col-xs12">
                <label class="layui-form-label">所属院系</label>
                <div class="layui-input-block">
                    <input type="text" class="layui-input courseName"
                           lay-verify="required" name="courseType" id="courseType" value="${(userInfo.courseType)!}" placeholder="请输入所属院系">
                </div>
            </div>
            <div class="layui-form-item layui-row layui-col-xs12">
                <label class="layui-form-label">教师姓名</label>
                <div class="layui-input-block">
                    <input type="text" class="layui-input courseName"
                           lay-verify="required" name="teacherName" value="${(userInfo.teacherName)!}"
                           id="teacherName"
                           placeholder="请输入教师姓名"><#--required : 必填项 -->
                </div>
            </div>

            <div class="layui-form-item layui-row layui-col-xs12">
                <label class="layui-form-label">手机号</label>
                <div class="layui-input-block">
                    <input type="text" class="layui-input teacherPhone"
                           lay-verify="teacherPhone" name="teacherPhone" value="${(userInfo.teacherPhone)!}" id="teacherPhone" placeholder="请输入手机号">
                </div>
            </div>


            <div class="layui-form-item layui-row layui-col-xs12">
                <label class="layui-form-label">角色</label>
                <div class="layui-input-block">
                    <select name="roleIds"  xm-select="selectId">
                    </select>
                </div>
            </div>


            <br/>
            <div class="layui-form-item layui-row layui-col-xs12">
                <div class="layui-input-block">
                    <button class="layui-btn layui-btn-lg" lay-submit=""
                            lay-filter="addOrUpdateUser">确认
                    </button>
                    <button class="layui-btn layui-btn-lg layui-btn-normal" id="closeBtn">取消</button>
                </div>
            </div>
        </form>

    <script type="text/javascript" src="${ctx}/js/user/add.update.js"></script>
    </body>
</html>