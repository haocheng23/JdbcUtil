package com.haocheng.utils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @description: 编写数据库连接池
 * @author: haocheng
 * @date: 2019-03-01 08:56
 *
 *
 * 实现DataSource接口，并实现连接池功能的步骤：
 *      1.在DataSource构造函数中批量创建与数据库的连接，并把创建的连接加入LinkedList对象中。
 *      2.实现getConnection方法，让getConnection方法每次调用时，从LinkedList中取一个Connection返回给用户。
 *      3.当用户使用完Connection，调用Connection.close()方法时，Collection对象应保证将自己返回到LinkedList中,而不要把conn还给数据库。
 *
 */
public class JdbcPool implements DataSource {


    /**
     * @Field: listConnections
     *      使用LinkedList集合来存放数据库链接
     *      由于要频繁读写List集合，所以这里使用LinkedList存储数据库连接比较合适
     */
    private  static LinkedList<Connection> listConnections = new LinkedList<Connection>();

    static {
        //在静态代码块中加载db.properties配置文件
        InputStream is = JdbcPool.class.getClassLoader().getResourceAsStream("db.properties");
        Properties prop = new Properties();
        try {
            prop.load(is);
            String driver = prop.getProperty("driver");
            String url = prop.getProperty("url");
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            //初始化数据库连接池连接数大小
            int jdbcPoolInitSize = Integer.parseInt(prop.getProperty("jdbcPoolInitSize"));
            //加载驱动
            Class.forName(driver);
            //批量创建连接
            for (int i = 0; i < jdbcPoolInitSize; i++) {
                Connection conn = DriverManager.getConnection(url, username, password);
                System.out.println("获取到了连接" + conn);
                //将数据库连接存到listConnections中，listConnections集合就是作为存放数据库连接的连接池
                listConnections.add(conn);
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }


    /**
     * @description: 获取数据库连接
     * @author: haocheng
     * @date: 2019-03-01 09:13
     *
     * 使用动态代理技术构建连接池中的connection
     */
    @Override
    public Connection getConnection() throws SQLException {
        if(listConnections.size() > 0){
            //从集合中获取到一个数据库连接
            final Connection conn = listConnections.removeFirst();
            System.out.println("数据库连接池大小是：" + listConnections.size());
            //返回Connection对象的代理对象
            return (Connection) Proxy.newProxyInstance(JdbcPool.class.getClassLoader(), conn.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (!method.getName().equals("close")){
                        return method.invoke(conn,args);
                    }else{
                        //若调用的是Connection对象的close方法，就把conn归还给数据库连接池
                        listConnections.add(conn);
                        System.out.println(conn + "被归还给数据库连接池了");
                        System.out.println("数据库连接池大小是：" + listConnections.size());
                        return null;
                    }
                }
            });

        }else{
            throw new RuntimeException("对不起，数据库忙");
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
