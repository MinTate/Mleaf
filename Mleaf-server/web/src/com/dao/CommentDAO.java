package com.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.db.DBManager;
import com.model.Comment;

/**
 *@author coolszy
 *@date Feb 23, 2012
 *@blog http://blog.92coding.com
 */
public class CommentDAO
{
	DBManager manager;
	String sql = "";
	ResultSet rs;
	
	public CommentDAO() throws IOException, ClassNotFoundException
	{
		manager = DBManager.createInstance();
	}
	
	/**
	 * 获取回复信息
	 * @param nid 新闻编号
	 * @param startRid 起始ID
	 * @param count 返回数量
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Comment> getComments(int nid,int startCid,int count) throws SQLException
	{
		ArrayList<Comment> list = new ArrayList<Comment>();
		sql = "select cid,nid,ptime,region,content,supportcount,opposecount from t_comment where nid="+nid+" and deleted=1 order by ptime desc limit"+startCid+","+count;
		manager.connectDB();
		rs = manager.executeQuery(sql);
		while (rs.next())
		{
			Comment comment = new Comment();
			comment.setCid(rs.getInt("cid"));
			comment.setNid(rs.getInt("nid"));
			comment.setPtime(rs.getString("ptime"));
			comment.setRegion(rs.getString("region"));
			comment.setContent(rs.getString("content"));
			comment.setSupportCount(rs.getInt("supportcount"));
			comment.setOpposeCount(rs.getInt("opposecount"));
			list.add(comment);
		}
		manager.closeDB();
		return list;
	}
	
	/**
	 * 保存新评论
	 * @param nid 新闻编号
	 * @param ptime 发表时间
	 * @param region 地区
	 * @param content 内容
	 * @throws SQLException
	 */
	public void addComment(int nid,String ptime,String region,String content) throws SQLException
	{
		sql = "INSERT INTO t_comment (nid,ptime,region,content) VALUES ("+nid+","+ptime +","+ region+","+content+")";
		manager.connectDB();
		manager.executeUpdate(sql);
		manager.closeDB();
	}
	
	/**
	 * 获取指定新闻评论数量
	 * @param nid
	 * @return
	 * @throws SQLException
	 */
	public long getSpecifyNewsCommentsCount(int nid) throws SQLException
	{
		long count = 0;
		sql = "select count(cid) as count from t_comment where nid="+nid;
		manager.connectDB();
		rs = manager.executeQuery(sql);
		if (rs.next())
		{
			count = rs.getLong("count");
		}
		return count;
	}
}
