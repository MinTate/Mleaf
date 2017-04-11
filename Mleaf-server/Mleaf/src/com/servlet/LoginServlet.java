package com.servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.LoginDAO;

public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 369840050351775312L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 接收客户端信息
        String username = request.getParameter("username");
        String password = request.getParameter("password");
   
        // 新建服务对象
        LoginDAO login = new LoginDAO();

        // 验证处理
        boolean loged = login.login(username, password);
        if (loged) {
            System.out.print("Succss");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print("true");
            out.flush();
            out.close();
           
        } else {
            System.out.print("Failed");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print("flase");
            out.flush();
            out.close();
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    				doGet(request, response);
    }

}