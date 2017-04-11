package com.example.myq.mleaf;

import com.example.myq.service.LoginService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by MYQ on 2016/3/7.
 */
public class LoginActivity  extends Activity implements View.OnClickListener{

    // 登陆按钮
    private Button login,register,forgetpassword;
    // 显示用户名和密码
    EditText username, password;
    // 创建等待框
    private ProgressDialog dialog;
    // 返回的数据

    HttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        client=new DefaultHttpClient();
        // 获取控件
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register=(Button)findViewById(R.id.register);
        forgetpassword=(Button)findViewById(R.id.forgetpassword);

        // 设置按钮监听器
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forgetpassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                // 检测网络，无法检测wifi
                if (!checkNetwork()) {
                    Toast toast = Toast.makeText(LoginActivity.this,"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                }
                if(username.getText().toString().equals("")==false&&password.getText().toString().equals("")==false){
                    // 提示框
                    dialog = new ProgressDialog(this);
                    dialog.setTitle("提示");
                    dialog.setMessage("正在登陆，请稍后...");
                    dialog.setCancelable(false);
                    dialog.show();

                    ReadNet("http://111.114.116.28:8080/Mleaf/LoginServlet", username.getText().toString(), password.getText().toString());
                }else{
                    if(username.getText().toString().equals("")){
                        Toast.makeText(LoginActivity.this, "手机号码不能为空", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                    }
                }

                break;

            //跳转到注册框
            case R.id.register:
                Intent regItn = new Intent(LoginActivity.this, VerifyActivity.class);
                regItn.putExtra("flag", "1");
                startActivity(regItn);
                break;
            case R.id.forgetpassword:
                Intent forgetItn = new Intent(LoginActivity.this, VerifyActivity.class);
                forgetItn.putExtra("flag", "2");
                startActivity(forgetItn);
                break;
        }
    }

    //登陆验证模块
    public void ReadNet(String url,String user,String pass){

        new AsyncTask<String,Void,String>(){

            @Override
            protected String doInBackground(String... params) {
                return  LoginService.executeHttpPost(params[0],params[1],params[2]);
            }

            protected void onPostExecute(String result){
                if(result.equals("true")){
                    Toast.makeText(LoginActivity.this, "发送成功", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(main);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "发送失败", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        }.execute(url,user,pass);
    }

    // 检测网络
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    public void onBackPressed() {
        finish();
    }
}
