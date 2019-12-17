package haidian.duty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DutyApplication {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(DutyApplication.class);
        ApplicationContext applicationContext = springApplication.run(args);

    }

}
