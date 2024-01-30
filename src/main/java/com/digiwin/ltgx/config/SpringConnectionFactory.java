package com.digiwin.ltgx.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.digiwin.ltgx.constant.Constants;
import com.digiwin.ltgx.utils.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;


/**
 * data source connection factory
 */
@Slf4j
@Configuration
@MapperScan("com.digiwin.ltgx.mapper")
public class SpringConnectionFactory {

    /**
     * pagination interceptor
     *
     * @return pagination interceptor
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * get the data source
     *
     * @return druid dataSource
     */
    @Bean(initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource() {

        DruidDataSource druidDataSource = new DruidDataSource();

        druidDataSource.setDriverClassName(PropertyUtils.getString(Constants.SPRING_DATASOURCE_DRIVER_CLASS_NAME));
        druidDataSource.setUrl(PropertyUtils.getString(Constants.SPRING_DATASOURCE_URL));
        druidDataSource.setUsername(PropertyUtils.getString(Constants.SPRING_DATASOURCE_USERNAME));
        druidDataSource.setPassword(PropertyUtils.getString(Constants.SPRING_DATASOURCE_PASSWORD));
        //druidDataSource.setValidationQuery(PropertyUtils.getString(Constants.SPRING_DATASOURCE_VALIDATION_QUERY, "SELECT 1"));

        druidDataSource.setPoolPreparedStatements(PropertyUtils.getBoolean(Constants.SPRING_DATASOURCE_POOL_PREPARED_STATEMENTS, true));
        druidDataSource.setTestWhileIdle(PropertyUtils.getBoolean(Constants.SPRING_DATASOURCE_TEST_WHILE_IDLE, true));
        druidDataSource.setTestOnBorrow(PropertyUtils.getBoolean(Constants.SPRING_DATASOURCE_TEST_ON_BORROW, true));
        druidDataSource.setTestOnReturn(PropertyUtils.getBoolean(Constants.SPRING_DATASOURCE_TEST_ON_RETURN, true));
        druidDataSource.setKeepAlive(PropertyUtils.getBoolean(Constants.SPRING_DATASOURCE_KEEP_ALIVE, true));

        druidDataSource.setMinIdle(PropertyUtils.getInt(Constants.SPRING_DATASOURCE_MIN_IDLE, 10));
        druidDataSource.setMaxActive(PropertyUtils.getInt(Constants.SPRING_DATASOURCE_MAX_ACTIVE, 90));
        druidDataSource.setMaxWait(PropertyUtils.getInt(Constants.SPRING_DATASOURCE_MAX_WAIT, 60000));
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(PropertyUtils.getInt(Constants.SPRING_DATASOURCE_MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE, 20));
        druidDataSource.setInitialSize(PropertyUtils.getInt(Constants.SPRING_DATASOURCE_INITIAL_SIZE, 50));
        druidDataSource.setTimeBetweenEvictionRunsMillis(PropertyUtils.getLong(Constants.SPRING_DATASOURCE_TIME_BETWEEN_EVICTION_RUNS_MILLIS, 60000));
        druidDataSource.setTimeBetweenConnectErrorMillis(PropertyUtils.getLong(Constants.SPRING_DATASOURCE_TIME_BETWEEN_CONNECT_ERROR_MILLIS, 60000));
        druidDataSource.setMinEvictableIdleTimeMillis(PropertyUtils.getLong(Constants.SPRING_DATASOURCE_MIN_EVICTABLE_IDLE_TIME_MILLIS, 300000));
        druidDataSource.setValidationQueryTimeout(PropertyUtils.getInt(Constants.SPRING_DATASOURCE_VALIDATION_QUERY_TIMEOUT, 10));
        //auto commit
        druidDataSource.setDefaultAutoCommit(PropertyUtils.getBoolean(Constants.SPRING_DATASOURCE_DEFAULT_AUTO_COMMIT, true));
        return druidDataSource;
    }

    /**
     * * get transaction manager
     *
     * @return DataSourceTransactionManager
     */
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * * get sql session factory
     *
     * @return sqlSessionFactory
     * @throws Exception sqlSessionFactory exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        configuration.setCallSettersOnNulls(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.addInterceptor(paginationInterceptor());
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setDataSource(dataSource());

        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setIdType(IdType.AUTO);
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setDbConfig(dbConfig);
        sqlSessionFactoryBean.setGlobalConfig(globalConfig);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.digiwin.ltgx.domain");
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("mapper/*Mapper.xml"));
        sqlSessionFactoryBean.setTypeEnumsPackage("com.digiwin.ltgx.enums");
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * get sql session
     *
     * @return SqlSession
     * @throws Exception
     */
    @Bean
    public SqlSession sqlSession() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

}
