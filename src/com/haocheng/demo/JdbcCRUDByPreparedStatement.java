package com.haocheng.demo;

import com.haocheng.utils.JdbcUtils;
import org.junit.Test;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description: 通过PreparedStatement对象完成对数据库的CRUD操作
 * @author: haocheng
 * @date: 2019-02-28 13:54
 *
 */
public class JdbcCRUDByPreparedStatement {

    @Test
    public void insert(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql = "insert into users (id,name,password,email,birthday) values (?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1,6);
            stmt.setString(2, "养乐多");
            stmt.setString(3, "123456");
            stmt.setString(4, "874774624@qq.com");
            String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(System.currentTimeMillis());
            stmt.setString(5, date);
            //执行插入操作，executeUpdate方法返回成功的条数
            int result = stmt.executeUpdate();
            if(result > 0){
                System.out.println("插入成功");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            //SQL执行完成之后释放相关资源
            JdbcUtils.release(conn,stmt,rs);
        }
    }


    @Test
    public void find(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "select * from users where id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1,2);
            rs = stmt.executeQuery();
            if (rs.next()){
                System.out.println(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(conn,stmt,rs);
        }
    }


    @Test
    public void update(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "update users set name = ?,email = ? where id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,"哈哈");
            stmt.setString(2,"987654321@qq.com");
            stmt.setInt(3,3);
            int result = stmt.executeUpdate();
            if(result > 0){
                System.out.println("更新成功");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(conn,stmt,rs);
        }
    }


    @Test
    public void delete(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql = "delete from users where id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1,1);
            int result = stmt.executeUpdate();
            if(result > 0){
                System.out.println("删除成功");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(conn,stmt,rs);
        }

    }

    @Test
    public void testParameterMetaData() throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql = "select * from users wherer name=? and password=?";
        //将SQL预编译一下
        PreparedStatement st = conn.prepareStatement(sql);
        ParameterMetaData pm = st.getParameterMetaData();
        //getParameterCount() 获得指定参数的个数
        System.out.println(pm.getParameterCount());
        //getParameterType(int param)：获得指定参数的sql类型，MySQL数据库驱动不支持
//        System.out.println(pm.getParameterType(1));
        JdbcUtils.release(conn, null, null);
    }
}
