package haidian.audio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(MainApplication.class);
        ApplicationContext applicationContext = springApplication.run(args);

    }

}
