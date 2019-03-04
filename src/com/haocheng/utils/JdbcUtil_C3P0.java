package com.haocheng.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @description: c3p0数据库连接工具类
 * @author: haocheng
 * @date: 2019-03-01 14:37
 *
 *   <---c3p0有自动回收空闲连接功能--->
 */
public class JdbcUtil_C3P0 {
    private static ComboPooledDataSource ds = null;
    //在静态代码块中创建数据库连接池
    static{
        try {
            /**
             * @Field: 方法一
             *     通过代码创建C3P0数据库连接池
             */
            /*ds = new ComboPooledDataSource();
            ds.setDriverClass("com.mysql.jdbc.Driver");
            ds.setJdbcUrl("jdbc:mysql://localhost:3306/jdbcstudy");
            ds.setUser("root");
            ds.setPassword("123456");
            ds.setInitialPoolSize(10);
            ds.setMinPoolSize(5);
            ds.setMaxPoolSize(20);*/

            /**
             * @Field: 方法二
             *     通过读取配置文件创建C3P0数据库连接池
             *     C3P0的xml配置文件c3p0-config.xml必须放在src目录下
             */
            //ds = new ComboPooledDataSource();//使用C3P0的缺省配置来创建数据源
            ds = new ComboPooledDataSource("MySQL");//使用C3P0的命名配置来创建数据源
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * @description: 从数据源中获取数据库连接
     * @author: haocheng
     * @date: 2019-03-01 14:44
     *
     */
    public static Connection getConnection() throws SQLException {
        //从数据源中获取数据库连接
        return ds.getConnection();
    }


    /**
     * @description: 释放资源
     * @author: haocheng
     * @date: 2019-03-01 14:45
     *
     */
    public static void release(Connection conn, Statement st, ResultSet rs){
        if(rs!=null){
            try{
                //关闭存储查询结果的ResultSet对象
                rs.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if(st!=null){
            try{
                //关闭负责执行SQL命令的Statement对象
                st.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(conn!=null){
            try{
                //将Connection连接对象还给数据库连接池
                conn.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
