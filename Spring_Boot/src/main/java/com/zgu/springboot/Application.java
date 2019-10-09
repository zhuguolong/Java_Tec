package com.zgu.springboot;

import com.zgu.springboot.util.limitflow.CurrentLimiter;
import com.zgu.springboot.util.limitflow.LimitRule;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 扫描 mapper 接口路径
@MapperScan(value = "com.zgu.**.mapper")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		// 限流规则
		LimitRule limitRule = new LimitRule("/zgu", 4);
		CurrentLimiter.addRule(limitRule);
	}

}
