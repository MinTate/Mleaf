package com.servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.SendTemplateSMS;

public class VerfityServlet extends HttpServlet {

    private static final long serialVersionUID = 369840050351775312L;

    // 接收客户端信息
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	String validateCode="";
    	String flag=request.getParameter("flag");
    	
    	if(flag.equals("1")){
    		    String username = request.getParameter("username");
    	        username = new String(username.getBytes("ISO-8859-1"), "UTF-8");

    	        // 新建服务对象
    	        SendTemplateSMS sendSMS=new SendTemplateSMS();

    	        // 发送验证短信
    	        boolean sign = sendSMS.sendMessage(username);
    	   	    
    	        // 返回是否发送成功
    	        if (sign) {
    	        	System.out.print("Succss");
        	        validateCode=sendSMS.validateCode;
    	            response.setCharacterEncoding("UTF-8");
    	            response.setContentType("text/html");
    	            PrintWriter out = response.getWriter();
    	            out.print(validateCode);
    	            out.flush();
    	            out.close();
    	           
    	        } else {
    	        	System.out.print("Fail");
        	        validateCode="";
    	            response.setCharacterEncoding("UTF-8");
    	            response.setContentType("text/html");
    	            PrintWriter out = response.getWriter();
    	            out.print("flase");
    	            out.flush();
    	            out.close();
    	        }
    	}
    	
    	if(flag.equals("2")){
    		if(validateCode.equals(request.getParameter("verification"))){
    			System.out.print("Succss2");
    			response.setCharacterEncoding("UTF-8");
	            response.setContentType("text/html");
	            PrintWriter out = response.getWriter();
	            out.print("true");
	            out.flush();
	            out.close();
    		}else {
    			System.out.print("Fail");
    	        validateCode="";
	            response.setCharacterEncoding("UTF-8");
	            response.setContentType("text/html");
	            PrintWriter out = response.getWriter();
	            out.print("flase");
	            out.flush();
	            out.close();
	        }
    		
    	}
    	     
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    				doGet(request, response);
    }

}