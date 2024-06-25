//package com.bridge.config;
//
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DataSourceConfig {
//
//    @Bean(name = "dataSource")
//    public DataSource getDataSource() {
//        DataSourceBuilder<?> builder = DataSourceBuilder.create();
//        builder = builder.driverClassName("com.mysql.cj.jdbc.Driver");
//        builder = builder.url("jdbc:mysql://localhost:3306/bridge");
//        builder = builder.username("root");
//        builder = builder.password("1234");
//        return builder.build();
//    }
//}
