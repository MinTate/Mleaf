package com.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import sun.misc.BASE64Decoder;

import com.dao.LoginDAO;
import com.jspsmart.upload.SmartUpload;
import com.service.LeafService;

public class LeafServlet extends HttpServlet {
	
	private static final long serialVersionUID = 369840050351775312L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		SmartUpload smartUpload = new SmartUpload();
		String msg=request.getParameter("msg");
		out.print(msg);  
        try {  
            smartUpload.initialize(this.getServletConfig(), request, response); 
            smartUpload.upload();  
            com.jspsmart.upload.File smartFile = smartUpload.getFiles().getFile(0);  
            if (!smartFile.isMissing()) {  
                String saveFileName = "F:/MyEclipse 10/Mleaf/WebRoot/temp/" + smartFile.getFileName();
                System.out.println(saveFileName);
                System.out.println(smartUpload.getSize());
                
                smartFile.saveAs(saveFileName, smartUpload.SAVE_PHYSICAL);
                
               // ArrayList<String> description=new ArrayList<String>();
                LeafService leafService=new LeafService();
                System.out.println(leafService.leaf(saveFileName));
                String[] des=leafService.leaf(saveFileName).split("\\|");
                for (int i=0;i<des.length;i++){
                	System.out.println(des[i]);
                }
                System.out.println(des[0]);
                String s="";
                for (int i=1;i<des.length;i++){
                    s=s+des[i]+"\n";
                }
                System.out.println(s);
                out.print(leafService.leaf(saveFileName));
            } else {  
                out.print("missing...");  
            }  
        } catch (Exception e) {  
        	out.print(e+","+msg);  
        } 
		out.flush();
		out.close();
	  }
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				doGet(request, response);
	}


}
