package com.du.controller;

import com.du.service.ifs.CommandService;
import com.fjhtxx.cloud.core.http.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Du YingJie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/21 14:53]
 */
@RestController
@RequestMapping("/linux")
@RequiredArgsConstructor
@CrossOrigin
public class CommandController {

    private final CommandService commandService;

    @GetMapping("/exec")
    public Resp<Boolean> execCommand(String cmd) {
        commandService.executeCmd(cmd);
        return Resp.ok();
    }

}
