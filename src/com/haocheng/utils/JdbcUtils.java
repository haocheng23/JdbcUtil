package com.haocheng.utils;


import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @description: 原始数据库连接工具类
 * @author: haocheng
 * @date: 2019-02-28 11:36
 */
public class JdbcUtils {
    private static String driver = null;
    private static String url = null;
    private static String username = null;
    private static String password = null;

    static {
        try {
            //读取properties数据库连接信息
            Properties prop = new Properties();
            InputStream is = JdbcUtils.class.getClassLoader().getResourceAsStream("db.properties");
            prop.load(is);
            // 使用InPutStream流读取properties文件
//            BufferedReader br = new BufferedReader(new FileReader("db.properties"));
//            prop.load(br);

            //获取数据库连接参数
            driver = prop.getProperty("driver");
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");

            //加载数据库驱动
            Class.forName(driver);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }

    }


    /**
     * @description: 获取conn连接对象
     * @author: haocheng
     * @date: 2019-02-28 12:00
     *
     * @params: 
     * @return: 
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }


    /**
     * @description: 释放资源(先创建的后关闭)
     * 要释放的资源包括：
     *      存储查询结果的ResultSet对象
     *      负责执行SQL命令的Statement对象
     *      数据库连接Connection对象
     * @author: haocheng
     * @date: 2019-02-28 13:47
     *
     * @params: Connection  PreparedStatement   ResultSet
     * @return:
     */
    public static void release(Connection conn, PreparedStatement stmt, ResultSet rs){
        if(rs != null){
            try {
                //关闭存储查询结果的ResultSet对象
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;
        }

        if(stmt != null){
             try{
                 //关闭负责执行SQL命令的Statement对象
                 stmt.close();
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


    /**
     * @description: 万能更新
     * @author: haocheng
     * @date: 2019-03-01 16:17
     *      所有实体的CUD操作代码基本相同，仅仅发送给数据库的SQL语句不同而已，
     *      因此可以把CUD操作的所有相同代码抽取到工具类的一个update方法中，并定义参数接收变化的SQL语句
     * @params: sql     要执行的SQL
     * @params: params[]    执行SQL时使用的参数数组
     * @return:
     */
    public static void update(String sql, Object params[]){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i+1,params[i]);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(conn,stmt,rs);
        }
    }

    /**
     * @description: 万能查询
     * @author: haocheng
     * @date: 2019-03-01 16:23
     *      实体的R操作，除SQL语句不同之外，根据操作的实体不同，对ResultSet的映射也各不相同，
     *      因此可义一个query方法，除以参数形式接收变化的SQL语句外，
     *      可以使用策略模式由qurey方法的调用者决定如何把ResultSet中的数据映射到实体对象中。
     * @params: sql 要执行的SQL
     * @params: params 执行SQL时使用的参数
     * @params: rsh 查询返回的结果集处理器
     * @return:
     */
    public static Object query(String sql, Object[] params, ResultSetHandler rsh) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i+1, params[i]);
            }
            rs = stmt.executeQuery();
            /**
             * @Field: 对于查询返回的结果集处理使用到了策略模式，
             *  在设计query方法时，query方法事先是无法知道用户对返回的查询结果集如何进行处理的，即不知道结果集的处理策略，
             *  那么这个结果集的处理策略就让用户自己提供，query方法内部就调用用户提交的结果集处理策略进行处理
             *  为了能够让用户提供结果集的处理策略，需要对用户提供一个结果集处理接口ResultSetHandler
             *  用户只要实现了ResultSetHandler接口，那么query方法内部就知道用户要如何处理结果集了
             */

            return rsh.handler(rs);

        } finally {
            release(conn, stmt, rs);
        }
    }

}
