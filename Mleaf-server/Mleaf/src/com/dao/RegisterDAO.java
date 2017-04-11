package com.dao;


import com.db.DBManager;

/**
 * 注册服务类
 * @author MYQ
 *
 */

public class RegisterDAO {

    public Boolean register(String username, String password) {
    	
        // 获取SQL插入语句
        String regSql = "replace into table_password values('"+ username+ "','"+ password+ "') ";

        // 获取DB对象
        DBManager sql = DBManager.createInstance();
        sql.connectDB();

        int ret = sql.executeUpdate(regSql);
        if (ret != 0) {
            sql.closeDB();
            return true;
        }
        sql.closeDB();
        
        return false;
    }
}
