package com.dubey.dubey_porfolio_java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DubeyPorfolioJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubeyPorfolioJavaApplication.class, args);
	}

}
//./gradlew clean build
//./gradlew bootRun
//./gradlew dependencies --configuration runtimeClasspath
// http://localhost:8081/actuator
// http://localhost:8080/swagger-ui/index.html
// http://localhost:8081/v3/api-docs

// docker build -t dubey-portfolio-java .
// docker run -p 8080:8080 dubey-portfolio-java
