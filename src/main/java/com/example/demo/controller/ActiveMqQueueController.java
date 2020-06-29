package com.example.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.Queue;

@RestController
@RequestMapping("/amq")
@Api(tags = "acticeMQ-producer", description = "acticeMQ-producer", value = "acticeMQ-producer")
public class ActiveMqQueueController {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    Queue queue;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ApiOperation(value="生产消息", position = 1, notes="生产消息")
    @ApiImplicitParams({
    })
    @ResponseBody
    public String send(){
        this.jmsMessagingTemplate.convertAndSend(this.queue, "这是activeMQ 消息");
        return "success";
    }
}
