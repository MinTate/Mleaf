package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.dao.CategoryDAO;
import com.model.Category;
import com.util.TextUtility;

/**
 * @author coolszy
 * @date Feb 19, 2012
 * @blog http://blog.92coding.com
 *       http://localhost:8080/web/getCategories?starttid=0&count=10
 */
public class GetCategoriesServlet extends HttpServlet
{
	private static final long serialVersionUID = -6534417657358062448L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		response.setContentType("text/html;charset=UTF-8");
		String startTidStr = request.getParameter("starttid");
		String countStr = request.getParameter("count");
		int startTid = 0;
		int count = 0;
		startTid = TextUtility.String2Int(startTidStr);
		count = TextUtility.String2Int(countStr);
		JSONObject jObject = new JSONObject();
		CategoryDAO typeDAO;
		try
		{
			typeDAO = new CategoryDAO();
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			ArrayList<Category> retList = typeDAO.getTypes(startTid, count);
			HashMap<String, Object> map;
			for (Category category : retList)
			{
				map = new HashMap<String, Object>();
				map.put("cid", category.getCid());
				map.put("title", category.getTitle());
				list.add(map);
			}
			JSONObject jObject2 = new JSONObject();
			jObject2.put("totalnum", retList.size());
			jObject2.put("info", list);

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

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		throw new NotImplementedException();
	}

	public static void main(String[] args) throws JSONException
	{
		// String json = "{\"name\":\"reiz\"}";
		// JSONObject jsonObj = new JSONObject();
		// String name = "reiz";
		//
		// jsonObj.put("initial", name.substring(0, 1).toUpperCase());
		//
		// String[] likes = new String[]
		// { "JavaScript", "Skiing", "Apple Pie" };
		// jsonObj.put("likes", likes);
		//
		// Map<String, String> ingredients = new HashMap<String, String>();
		// ingredients.put("apples", "3kg");
		// ingredients.put("sugar", "1kg");
		// ingredients.put("pastry", "2.4kg");
		// ingredients.put("bestEaten", "outdoors");
		// jsonObj.put("ingredients", ingredients);
		//
		// System.out.println(jsonObj);

		JSONObject jObject = new JSONObject();
		CategoryDAO categoryDAO;
		try
		{
			categoryDAO = new CategoryDAO();
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			ArrayList<Category> retList = categoryDAO.getTypes(0, 2);
			HashMap<String, Object> map;
			for (Category category : retList)
			{
				map = new HashMap<String, Object>();
				map.put("cid", category.getCid());
				map.put("title", category.getTitle());
				list.add(map);
			}
			JSONObject jObject2 = new JSONObject();
			jObject2.put("totalnum", retList.size());
			jObject2.put("info", list);

			jObject.put("ret", 0);
			jObject.put("msg", "ok");
			jObject.put("errcode", 0);
			jObject.put("data", jObject2);

			System.out.println(jObject);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
