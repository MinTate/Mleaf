package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.dao.CommentDAO;
import com.dao.NewsDAO;
import com.model.News;
import com.util.TextUtility;

/**
 *@author coolszy
 *@date Feb 19, 2012
 *@blog http://blog.92coding.com
 *http://localhost:8080/web/getNews?nid=1
 */
public class GetNewsServlet extends HttpServlet
{
	private static final long serialVersionUID = -7715894432269979527L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		response.setContentType("text/html;charset=UTF-8");
		String nidStr= request.getParameter("nid");
		int nid = 0;
		nid = TextUtility.String2Int(nidStr);
		JSONObject jObject = new JSONObject();
		try
		{
			CommentDAO commentDAO = new CommentDAO();
			long commentCount = commentDAO.getSpecifyNewsCommentsCount(nid);
			NewsDAO newsDAO = new NewsDAO();
			News news = newsDAO.getNews(nid);
			JSONObject jObject2 = new JSONObject();
			if (!TextUtility.isNull(news.getTitle()))
			{
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("nid", news.getNid());
				hashMap.put("title", news.getTitle());
				hashMap.put("body", news.getBody());
				hashMap.put("source", news.getSource());
				hashMap.put("replycount", commentCount);
				hashMap.put("ptime", news.getPtime());
				hashMap.put("imgsrc", news.getImgSrc());
				jObject2.put("news", hashMap);
			}
			jObject.put("ret", 0);
			jObject.put("msg", "ok");
			jObject.put("data", jObject2);
		} catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				jObject.put("ret", 1);
				jObject.put("msg", e.getMessage());
				jObject.put("data", "");
			} catch (JSONException ex)
			{
				ex.printStackTrace();
			}
		}
		PrintWriter out = response.getWriter();
		out.println(jObject);
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		throw new NotImplementedException();
	}
}
