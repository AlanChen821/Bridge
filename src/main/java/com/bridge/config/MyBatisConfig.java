package com.bridge.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.apache.ibatis.type.LocalDateTypeHandler;
import org.apache.ibatis.type.LocalTimeTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan("com.bridge.mapper")
public class MyBatisConfig {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

//    protected <T> T getMapper(DataSource dataSource, String mapperXmlFile, Class<T> mapperClass) throws Exception {
//        // Create session
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//
//        // Set datasource
//        sqlSessionFactoryBean.setDataSource(dataSource);
//
//        // Set mapper file and mapper class
//        sqlSessionFactoryBean.setMapperLocations(new Resource[] { new ClassPathResource(mapperXmlFile, mapperClass) });
//
//        // Set type handler for LocalTime and LocalDateTime
//        sqlSessionFactoryBean.setTypeHandlers(new TypeHandler<?>[] { new LocalDateTypeHandler(),
//                new LocalTimeTypeHandler(), new LocalDateTimeTypeHandler() });
//
//        // Create mapper bean
//        MapperFactoryBean<T> mapperFactoryBean = new MapperFactoryBean<>();
//        mapperFactoryBean.setSqlSessionFactory(sqlSessionFactoryBean.getObject());
//        mapperFactoryBean.setMapperInterface(mapperClass);
//
//        return mapperFactoryBean.getObject();
//    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
//        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return sessionFactory.getObject();
    }
}
