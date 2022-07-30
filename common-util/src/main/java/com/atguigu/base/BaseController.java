package com.atguigu.base;

import org.springframework.ui.Model;

/**
 * 包名:com.atguigu.base
 *
 * @author Leevi
 * 日期2022-06-13  09:31
 */
public class BaseController {
    private static final String PAGE_SUCCESS = "common/successPage";
    public String successPage(Model model,String successMessage){
        //1.将成功信息放入请求域
        model.addAttribute("messagePage",successMessage);
        //2.显示成功页面
        return PAGE_SUCCESS;
    }
}
