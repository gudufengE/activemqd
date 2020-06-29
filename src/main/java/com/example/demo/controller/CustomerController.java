package com.example.demo.controller;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.RestController;

/**
 * activeMQ消费者
 */
@RestController
public class CustomerController {

    /**
     * 监听和读取信息
     * @param s
     */
    @JmsListener(destination = "active.queue")
    public void readActiveMQQueue(String s){
        System.out.println(s);
    }
}
