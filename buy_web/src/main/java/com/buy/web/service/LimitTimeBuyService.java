package com.buy.web.service;

import com.buy.api.LimitTimeBuyServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("provider-service")
public interface LimitTimeBuyService extends LimitTimeBuyServiceApi {
}
