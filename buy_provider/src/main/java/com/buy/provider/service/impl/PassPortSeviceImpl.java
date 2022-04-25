package com.buy.provider.service.impl;

import com.buy.mapper.TbUserMapper;
import com.buy.pojo.TbUser;
import com.buy.pojo.TbUserExample;
import com.buy.provider.service.PassPortSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassPortSeviceImpl implements PassPortSevice {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Override
    public TbUser loginUser(String username, String password) {
        TbUserExample exp = new TbUserExample();
        exp.createCriteria().andUsernameEqualTo(username).andPasswordEqualTo(password);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(exp);
        if(tbUsers != null && tbUsers.size() ==1){
            return tbUsers.get(0);
        }

        return null;
    }
}
