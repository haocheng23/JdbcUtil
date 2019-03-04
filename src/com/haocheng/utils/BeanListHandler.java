package com.haocheng.utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 将结果集转换成bean对象的list集合的处理器
 * @author: haocheng
 * @date: 2019-03-01 17:15
 * 
 */
public class BeanListHandler implements ResultSetHandler{

    private Class<?> clazz;

    public BeanListHandler(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object handler(ResultSet rs) {
        try {
            List<Object> list = new ArrayList<Object>();
            while(rs.next()) {
                Object bean = clazz.newInstance();

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    String name = metaData.getColumnName(i + 1);
                    Object value = rs.getObject(i + 1);

                    Field f = bean.getClass().getDeclaredField(name);
                    f.setAccessible(true);
                    f.set(bean, value);
                }
                list.add(bean);
            }
            return list.size()>0?list:null;
        }catch (Exception e){
            throw new RuntimeException(e);

        }

    }
}
