package com.onlydive.onlydive;

import com.onlydive.onlydive.config.OpenApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(OpenApiConfig.class)

public class OnlydiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlydiveApplication.class, args);
	}

}
