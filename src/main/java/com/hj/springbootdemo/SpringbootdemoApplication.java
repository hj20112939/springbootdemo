package com.hj.springbootdemo;

import com.hj.springbootdemo.rabbitMq.MessageSender;
import com.hj.springbootdemo.smsSend.SmsSendUtil;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAsync
@SpringBootApplication
@Configuration
public class SpringbootdemoApplication {
	@Autowired
	private SmsSendUtil smsSendUtil;

	@RequestMapping("/")
	public String index(){
		return "Hello Spring Boot";
	}
	@RequestMapping("/sentMsgTest")
	public void sentMsgTest(){
		smsSendUtil.sendMessage("springbootdemo", "8618600250824", "sendMsgTest");
	}
	public static void main(String[] args) {
		SpringApplication.run(SpringbootdemoApplication.class, args);
	}

}
