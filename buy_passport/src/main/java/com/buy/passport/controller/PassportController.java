package com.buy.passport.controller;

import com.buy.commons.MtResult;
import com.buy.passport.service.UserService;
import com.buy.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;

@RestController
public class PassportController {
    @Autowired
    private UserService userService;
    @PostMapping("/user/login")
    public MtResult loginUser(String username, String password, HttpSession session){
        System.out.println(username);
        System.out.println(password);
        TbUser user = userService.loginUser(username, password);
        System.out.println("user: "+user);
        if(user != null){
            //放入session,并共享
            session.setAttribute("loginUser", user);
            return MtResult.ok();
        }else{
            return MtResult.error("用户名或密码有误");
        }
    }
}
