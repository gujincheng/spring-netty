package com.digiwin.ltgx.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * @Author zhangcq
 * @Date 2021/10/21 11:48
 * @Version 1.0
 * @Description 通过bean 工厂获取 spring bean
 */
@Slf4j
@Component
public class BeanFactoryContext implements BeanFactoryAware {

    private static BeanFactory beanFactory; //BEAN工厂

    /**
     * 根据bean的名字找bean的对象
     *
     * @param name
     * @return
     */
    public static Object findBeanByName(String name) {
        Object obj = null;
        try {
            obj = beanFactory.getBean(name);
        } catch (Exception e) {
            log.info(" find bean by name error : {} ", e);
        }
        return obj;
    }

    /**
     * 根据bean的class找bean的对象
     *
     * @param cls
     * @return
     */
    public static <T> T findBeanByClass(Class<T> cls) {
        T obj = null;
        try {
            obj = beanFactory.getBean(cls);
        } catch (Exception e) {
            log.info(" find bean by class error : {} ", e);
        }
        return obj;
    }

    /**
     * 是否存在bean
     *
     * @param name
     * @return
     */
    public static Object containsBean(String name) {
        Object obj = null;
        try {
            obj = beanFactory.containsBean(name);
        } catch (Exception e) {
            log.info(" locat bean by name error : {} ", e);
        }
        return obj;
    }

    @Override
    public void setBeanFactory(BeanFactory f) throws BeansException {
        beanFactory = f;
    }

}