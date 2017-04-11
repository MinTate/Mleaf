package com.servlet;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaveResult extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 // 接收客户端信息	
        String assess = request.getParameter("assess");
        String result = request.getParameter("result");
        result = new String(result.getBytes("iso8859-1"),"UTF-8");
        String fileName ="F:/MyEclipse 10/Mleaf/WebRoot/temp/"+request.getParameter("fileName");
        
        File beforefile=new File(fileName);
        
        if (assess.equals("right")) {        	

        	//这是你要保存之后的文件，是自定义的，本身不存在
        	File afterfile = new File("F:/MyEclipse 10/Mleaf/WebRoot/images/"+result+"_right_"+getPhotoFileName());

        	//定义文件输入流，用来读取beforefile文件
        	FileInputStream fis = new FileInputStream(beforefile);

        	//定义文件输出流，用来把信息写入afterfile文件中
        	FileOutputStream fos = new FileOutputStream(afterfile);

        	//文件缓存区
        	byte[] b = new byte[1024];
        	//将文件流信息读取文件缓存区，如果读取结果不为-1就代表文件没有读取完毕，反之已经读取完毕
        	while(fis.read(b)!=-1){
        	//将缓存区中的内容写到afterfile文件中
        	fos.write(b);
        	fos.flush();
        	
        	response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print("true");
            out.flush();
            out.close();
        	}
           }
        if (assess.equals("error")) {        	

        	//这是你要保存之后的文件，是自定义的，本身不存在
        	File afterfile = new File("F:/MyEclipse 10/Mleaf/WebRoot/images/"+result+"_error_"+getPhotoFileName());

        	//定义文件输入流，用来读取beforefile文件
        	FileInputStream fis = new FileInputStream(beforefile);

        	//定义文件输出流，用来把信息写入afterfile文件中
        	FileOutputStream fos = new FileOutputStream(afterfile);

        	//文件缓存区
        	byte[] b = new byte[1024];
        	//将文件流信息读取文件缓存区，如果读取结果不为-1就代表文件没有读取完毕，反之已经读取完毕
        	while(fis.read(b)!=-1){
        	//将缓存区中的内容写到afterfile文件中
        	fos.write(b);
        	fos.flush();
        	
        	response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print("true");
            out.flush();
            out.close();
        	}
           }
			
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
	
	 // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(date) + ".png";
    }

}
