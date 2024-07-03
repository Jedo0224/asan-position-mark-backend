package org.asanpositioningserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class AsanPositioningServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsanPositioningServerApplication.class, args);
	}

}
