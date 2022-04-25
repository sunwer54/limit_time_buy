package com.buy.api;

import com.buy.pojo.TbUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserServiceApi {
    @PostMapping("/user/login")
    public TbUser loginUser(@RequestParam("username") String username, @RequestParam("password") String password);

}
