package com.example.myq.service;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

/**
 * 登陆服务的功能类
 * @author MYQ
 * @time 2016/3/7.
 */
public class LoginService {

    // 通过 POST 方式获取HTTP服务器数据
    public static String executeHttpPost(String path,String username, String password) {

        try {
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("password", password);

            return sendPOSTRequest(path, params, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 处理发送数据请求
    private static String sendPOSTRequest(String path, Map<String, String> params, String encoding) throws Exception {

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();  //定义键值对队列
        //遍历map，将键值对信息存入NameValuePair类型的队列中
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        //编码队列表，在http 发送post请求时往往是非常有用的
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, encoding);
        HttpPost post = new HttpPost(path);
        post.setEntity(entity);
        HttpClient client = new DefaultHttpClient();
        // 请求超时
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
        // 读取超时
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);

        HttpResponse response = client.execute(post);
        // 判断是否成功收取信息
        if (response.getStatusLine().getStatusCode() == 200) {
             return getInfo(response);
        }else {
            // 未成功收取信息，返回空指针
            return null;
        }
    }

    // 收取数据
    private static String getInfo(HttpResponse response) throws Exception {

        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();

        byte[] data = read(is);
        // 转化为字符串
        String string=new String(data, "UTF-8");
        return  string;
    }

    // 将输入流转化为byte型
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }
}