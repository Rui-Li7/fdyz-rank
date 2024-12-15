package me.rui.fdyzrank;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("me.rui.fdyzrank.mapper")
@SpringBootApplication
public class FdyzRankApplication {

    public static void main(String[] args) {
        SpringApplication.run(FdyzRankApplication.class, args);
    }

}
