package rms.backend.config;

import javax.servlet.annotation.WebServlet;
import javax.sql.DataSource;

import org.apache.catalina.servlets.WebdavServlet;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan(value="rms.backend.mapper", sqlSessionFactoryRef="dbSqlSessionFactory")
@EnableTransactionManagement
public class DBConfig {

    @Bean(name = "dbDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.db.datasource")
    public DataSource db1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dbSqlSessionFactory")
    @Primary
    public SqlSessionFactory dbSqlSessionFactory(@Qualifier("dbDataSource") DataSource dbDataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dbDataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "dbSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate dbSqlSessionTemplate( SqlSessionFactory dbSqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(dbSqlSessionFactory);
    }
    //@Bean
    //public ServletRegistrationBean h2servletRegistration(){
    //    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
    //     registration.addUrlMappings("/console/*");
    //     return registration;
    //}
}
