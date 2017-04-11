package com.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.db.DBManager;
import com.model.Category;

/**
 *@author coolszy
 *@date Feb 19, 2012
 *@blog http://blog.92coding.com
 */
public class CategoryDAO
{
	DBManager manager;
	String sql = "";
	ResultSet rs;
	
	public CategoryDAO() throws IOException, ClassNotFoundException
	{
		manager = DBManager.createInstance();
	}
	
	/**
	 * 获取新闻类型
	 * @param startTid 起始类型编号
	 * @param count 数量
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Category> getTypes(int startTid,int count) throws SQLException
	{
		ArrayList<Category> list = new ArrayList<Category>();
		sql = "select cid,title from t_category where deleted = 1 order by sequnce LIMIT" +startTid+","+count;
		manager.connectDB();
		rs = manager.executeQuery(sql);
		while(rs.next())
		{
			Category category = new Category(rs.getInt("cid"), rs.getString("title"));
			list.add(category);
		}
		manager.closeDB();
		return list;
	}
	
	public void add(Category category)
	{
		throw new NotImplementedException();
	}
	
	public void update(Category category)
	{
		throw new NotImplementedException();
	}
	
	public void delete(int tid)
	{
		throw new NotImplementedException();
	}
	
	public ArrayList<Category> getAllTypes()
	{
		throw new NotImplementedException();
	}
}
