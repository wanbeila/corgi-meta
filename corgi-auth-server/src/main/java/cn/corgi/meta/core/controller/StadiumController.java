package cn.corgi.meta.core.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@RestController
@RequestMapping("api/stadium")
public class StadiumController {

    @PostMapping("query")
    public String query() {
        return "all";
    }
}
