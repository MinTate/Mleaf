package com.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.db.DBManager;

/**
 * 登陆服务类
 * @author MYQ
 *
 */

public class LoginDAO {

    public Boolean login(String username, String password) {

        // 获取SQL查询语句
        String logSql = "select * from table_password where username='" + username
                + "' and password ='" + password + "'";

        // 获取DB对象
        DBManager sql = DBManager.createInstance();
        sql.connectDB();

        // 操作DB对象
        try {
            ResultSet rs = sql.executeQuery(logSql);
            if (rs.next()) {
                	sql.closeDB();
                    return true;  	
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql.closeDB();
        return false;
    }

 
}