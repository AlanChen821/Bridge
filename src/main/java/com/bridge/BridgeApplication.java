package com.bridge;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@Slf4j
@MapperScan("com.bridge.mapper")
public class BridgeApplication {

    public static void main(String[] args) {
		SpringApplication.run(BridgeApplication.class, args);
	}

}
