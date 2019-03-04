package com.haocheng.demo;

import com.haocheng.dao.UserDao;
import com.haocheng.utils.JdbcUtil_C3P0;
import com.haocheng.utils.JdbcUtil_DBCP;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @description: 测试DBCP数据源
 * @author: haocheng
 * @date: 2019-03-01 13:25
 * 
 */
public class DataSourceTest {

    /**
     * @Field: dbcp数据源连接池测试
     *
     */
    @Test
    public void dbcpDataSourceTest(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil_DBCP.getConnection();
            String sql = "insert into users (name) values (?)";
            /**
             * @Field:
             *       5.1.17版本之后的mysql-connector增加了返回GeneratedKeys的条件，如果需要返回GeneratedKeys，
             *       则PreparedStatement需要显示添加一个参数PreparedStatement.RETURN_GENERATED_KEYS。
             */
            stmt = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
//            stmt.setInt(1,8);
            stmt.setString(1,"詹姆斯");
            stmt.executeUpdate();
            //获取数据库自动生成的主键
            rs = stmt.getGeneratedKeys();
            if(rs.next()){
                System.out.println(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil_DBCP.release(conn,stmt,rs);
        }
    }



    /**
     * @Field: c3p0数据源连接池测试
     *
     */
    @Test
    public void c3p0DataSourceTest(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            //获取数据库连接
            conn = JdbcUtil_C3P0.getConnection();
            String sql = "insert into users(name) values(?)";
            stmt = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, "韦德");
            stmt.executeUpdate();
            //获取数据库自动生成的主键
            rs = stmt.getGeneratedKeys();
            if(rs.next()){
                System.out.println(rs.getInt(1));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            //释放资源
            JdbcUtil_C3P0.release(conn, stmt, rs);
        }
    }



}
