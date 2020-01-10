package haidian.duty.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

//@Service
public class ThreadService {

    @Async
    public String test(){
        System.out.println("----------执行线程------------");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "处理完毕";
    }

}
