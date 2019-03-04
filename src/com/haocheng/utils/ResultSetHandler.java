package com.haocheng.utils;

import java.sql.ResultSet;

/**
 * @description: 结果集处理器接口
 * @author: haocheng
 * @date: 2019-03-01 17:01
 *
 */
public interface ResultSetHandler {

    /**
     * @description: 结果集处理方法
     * @author: haocheng
     * @date: 2019-03-01 17:02
     *
     * @params: rs 查询结果集
     * @return:
     */
    public Object handler(ResultSet rs);
}
