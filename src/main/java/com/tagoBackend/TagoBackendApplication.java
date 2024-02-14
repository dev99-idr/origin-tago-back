package com.tagoBackend;

import com.tagoBackend.control.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication
public class TagoBackendApplication {
	private final Logger log = LoggerFactory.getLogger(LoginController.class);

	public static void main(String[] args) {
		System.setProperty("DAS3.Config", "conf/das3.xml");
		SpringApplication.run(TagoBackendApplication.class, args);
	}
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
