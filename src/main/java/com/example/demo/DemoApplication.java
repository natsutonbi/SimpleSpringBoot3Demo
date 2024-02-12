package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
//some
import lombok.extern.slf4j.Slf4j;
@Slf4j
@SpringBootApplication
// @NacosPropertySource(dataId = "auth",autoRefreshed = true)
// @EnableDiscoveryClient
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		log.info("demo app is running");
	}
}