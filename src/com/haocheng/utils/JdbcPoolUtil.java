package com.haocheng.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @description: 测试原始数据库连接池
 * @author: haocheng
 * @date: 2019-03-01 11:37
 * 
 */
public class JdbcPoolUtil {

    /**
     * @Field: pool
     *      数据库连接池
     */
    private static JdbcPool pool = new JdbcPool();

    /**
     * @description: 从数据库连接池中获取数据库连接对象
     * @author: haocheng
     * @date: 2019-03-01 11:40
     * 
     * @params:
     * @return: Connection数据库连接对象
     */
    public static Connection getConnection() throws SQLException {
        return pool.getConnection();
    }


    /**
     * @description: 释放资源
     * @author: haocheng
     * @date: 2019-03-01 11:41
     * 
     * @params:
     * @return: 
     */
    public static void release(Connection conn, Statement st, ResultSet rs){
        if(rs != null){
            try{
                //关闭存储查询结果的ResultSet对象
                rs.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if(st != null){
            try{
                //关闭负责执行SQL命令的Statement对象
                st.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(conn != null){
            try{
                //关闭Connection数据库连接对象
                conn.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
