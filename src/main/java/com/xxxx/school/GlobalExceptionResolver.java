package com.xxxx.school;

import com.alibaba.fastjson.JSON;
import com.xxxx.school.base.ResultInfo;
import com.xxxx.school.exceptions.ParamsException;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component//让全局异常生效 交给lc管理 实例化
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        /**
         * 设置默认异常处理（返回视图）
         */
        ModelAndView modelAndView = new ModelAndView("error");
        // 设置异常信息
        modelAndView.addObject("code",500);
        modelAndView.addObject("msg","系统异常，请重试...");

        // 判断HandlerMethod
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取⽅法上的 ResponseBody 注解
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            if (responseBody == null){
                if (ex instanceof ParamsException) {
                    /**
                     * 方法返回视图
                     */
                    ParamsException p = (ParamsException) ex;
                    // 设置异常信息
                    modelAndView.addObject("code",p.getCode());
                    modelAndView.addObject("msg",p.getMsg());
                } /*else if (ex instanceof AuthException) { // 认证异常
                    AuthException a  = (AuthException) ex;
                    // 设置异常信息
                    modelAndView.addObject("code",a.getCode());
                    modelAndView.addObject("msg",a.getMsg());
                }*/
                return modelAndView;
            }else {
                /**
                 * 方法返回数据
                 *      返回通常是返回封装类，resultInfo
                 *      通过流把resultInfo输出，可能会出现中文乱码与响应格式问题，所以需要设置响应格式与编码
                 */
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("异常，请重试！");
                if (ex instanceof ParamsException) {
                    ParamsException p = (ParamsException) ex;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());

                } /*else if (ex instanceof AuthException) { // 认证异常
                    AuthException a = (AuthException) ex;
                    resultInfo.setCode(a.getCode());
                    resultInfo.setMsg(a.getMsg());
                }*/
                // 设置响应类型及编码格式（响应JSON格式的数据）
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = null;
                try {
                    // 得到输出流
                    out = response.getWriter();
                    // 将需要返回的对象转换成JOSN格式的字符
                    String json = JSON.toJSONString(resultInfo);
                    // 输出数据
                    out.write(json);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // 如果对象不为空，则关闭
                    if (out != null) {
                        out.close();
                    }
                }
                // 最终返回的是json格式
                return null;
            }
        }
        return modelAndView;
    }
}
