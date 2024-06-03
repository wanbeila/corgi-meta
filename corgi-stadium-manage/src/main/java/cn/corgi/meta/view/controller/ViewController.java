package cn.corgi.meta.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wanbeila
 * @date 2024/6/3
 */
@Controller
@RequestMapping("view")
public class ViewController {

    @GetMapping("main")
    public ModelAndView mainView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("main");
        return modelAndView;
    }
}
