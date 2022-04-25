package com.buy.provider.controller;

import com.buy.api.UserServiceApi;
import com.buy.pojo.TbUser;
import com.buy.provider.service.PassPortSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PassServiceController implements UserServiceApi {
    @Autowired
    private PassPortSevice passPortSevice;
    @Override
    public TbUser loginUser(String username, String password) {
        return passPortSevice.loginUser(username, password);
    }
}
