package com.haocheng.utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @description: 将结果集转换成bean对象的处理器
 * @author: haocheng
 * @date: 2019-03-01 17:04
 * 
 */
public class BeanHandler implements ResultSetHandler {
    private Class<?> clazz;

    public BeanHandler(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object handler(ResultSet rs) {
        try {
            if (!rs.next()){
                return null;
            }
            Object bean = clazz.newInstance();// 创建一个实例
            //得到结果集元数据
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                String name = metaData.getColumnName(i+1);//得到每列的列名
                Object value = rs.getObject(i + 1);
                Field f = clazz.getDeclaredField(name);//反射出类上列名对应的属性
                f.setAccessible(true);// 将目标属性设置为可以访问
                f.set(bean, value);//将属性值重新赋值
            }
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
}
