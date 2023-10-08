package com.yusuf.socketioserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SocketIoServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocketIoServerApplication.class, args);
	}

}
