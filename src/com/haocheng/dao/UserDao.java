package com.haocheng.dao;

import com.haocheng.bean.User;
import com.haocheng.utils.BeanHandler;
import com.haocheng.utils.BeanListHandler;
import com.haocheng.utils.JdbcUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * @description: 自定义JDBC框架测试类
 * @author: haocheng
 * @date: 2019-03-01 17:50
 *
 */
public class UserDao {

    public void add(User user) throws SQLException{
        String sql = "insert into users(name,password) values (?,?)";
        Object[] params = {user.getName(), user.getPassword()};
        JdbcUtils.update(sql,params);
    }

    public void delete(int id) throws SQLException{
        String sql = "delete from users where id=?";
        Object params[] = {id};
        JdbcUtils.update(sql, params);
    }

    public void update(User user) throws SQLException{

        String sql = "update account set name=?,password=? where id=?";
        Object params[] = {user.getName(),user.getPassword(),user.getId()};
        JdbcUtils.update(sql, params);

    }

    public User find(int id) throws SQLException {
        String sql = "select * from users where id = ?";
        Object[] params = {id};
        return (User) JdbcUtils.query(sql, params, new BeanHandler(User.class));
    }

    public List<User> getAll() throws SQLException{
        String sql = "select * from users";
        Object params[] = {};
        return (List<User>) JdbcUtils.query(sql, params,new BeanListHandler(User.class));
    }


    public static void main(String args[]) throws SQLException {
        UserDao userDao = new UserDao();
        User user = userDao.find(3);
        System.out.println(user.toString());
    }

}
