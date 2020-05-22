package com.lc.service.serviceImpl;

import com.lc.mapper.HelloMapper;
import com.lc.service.HelloService;
import com.lc.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: lc
 * @CreateDate: 2020/05/02 15:13
 **/
@Service
public class HelloServiceImpl implements HelloService {

    @Autowired
    HelloMapper helloMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public String getTime() {
        String time = (String) redisUtil.get("time");
        if (null == time) {
            time = helloMapper.getTime();
            redisUtil.set("time",time,30);
        }
        return time;
    }
}
