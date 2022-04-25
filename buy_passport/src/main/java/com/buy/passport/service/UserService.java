package com.buy.passport.service;

import com.buy.api.UserServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("provider-service")
public interface UserService extends UserServiceApi {
}
