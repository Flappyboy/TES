package top.jach.tes.app;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component//被spring容器管理
@Order(100)//如果多个自定义ApplicationRunner，用来标明执行顺序
public class AppRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

    }
}
