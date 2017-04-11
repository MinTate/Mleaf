package com.example.myq.mleaf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myq.service.LoginService;
import com.example.myq.service.VerifyService;

/**
 * Created by MYQ on 2016/3/8.
 */
public class VerifyActivity extends Activity implements View.OnClickListener{

    // 登陆按钮
    private Button next,sendverification;
    // 显示用户名和密码
    EditText username, verification;
    //显示标题提示栏
    TextView title_tip;
    // 创建等待框
    private ProgressDialog dialog;
    // 返回的数据
    private String info,phonenumber;
    //获取从服务器获得验证码
    private String validateCode ="";

    private  String flag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.verify);

        // 获取控件
        username = (EditText) findViewById(R.id.username);
        verification = (EditText) findViewById(R.id.verification);
        sendverification = (Button) findViewById(R.id.sendverification);
        next= (Button) findViewById(R.id.next);
        title_tip=(TextView)findViewById(R.id.title_tip);

        //从LoginActivity获取传递过来的flag值
        Intent intent=getIntent();
        flag=intent.getStringExtra("flag");

        //通过判断flag的值 来确定标题提示
        if(flag.equals("1")==true){
            title_tip.setText("注册");
        }
        if(flag.equals("2")==true){
            title_tip.setText("找回密码");
        }

        // 设置按钮监听器
        sendverification.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendverification:
                /*发生短信验证码操作 首先验证网络是否连接 然后判断手机号码输入框是否为空*/
                if (!checkNetwork()) {
                    Toast toast = Toast.makeText(VerifyActivity.this,"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                }
                String temp1=username.getText().toString();
                if(temp1.equals("")==false){
                    ReadNetOne(temp1);

                }else {
                    Toast.makeText(VerifyActivity.this, "手机号码不能为空！", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.next:
                /*验证短信验证码是否正确操作 首先验证网络是否连接 然后判断手机号码输入框和验证码输入框是否为空*/
                if (!checkNetwork()) {
                    Toast toast = Toast.makeText(VerifyActivity.this,"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                }
                String temp2=verification.getText().toString();
                String temp3=username.getText().toString();
                if(temp2.equals("")==false&&temp3.equals("")==false){
                    ReadNetTwo(temp2);
                }else{
                    if(temp3.equals("")){
                        Toast.makeText(VerifyActivity.this, "手机号码不能为空", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(VerifyActivity.this, "验证码不能为空", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }
    /*第一步：发送短信*/
    public void ReadNetOne(String phonenumber){

        new AsyncTask<String,Void,String>(){

            @Override
            protected String doInBackground(String... params) {
                validateCode=VerifyService.executeHttpPostOne(params[0]);
                return validateCode;
            }

            protected void onPostExecute(String result){
                if(result.equals("flase")){
                    Toast.makeText(VerifyActivity.this, "发送失败", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(VerifyActivity.this, "发送成功", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(phonenumber);
    }
    /*第二步：验证码验证模块*/
    public void ReadNetTwo(String ver){

        new AsyncTask<String,Void,String>(){

            @Override
            protected String doInBackground(String... params) {
                if (validateCode.equals(params[0])){
                    return "true";
                }else {
                    return "false";
                }
            }

            protected void onPostExecute(String result){
                /*如果验证成功，跳转至RegisterActivity,同时将phonenumber传递给RegisterActivity*/
                if(result.equals("true")){
                    Intent regItn = new Intent(VerifyActivity.this, RegisterActivity.class);
                    regItn.putExtra("phonenumber", phonenumber);
                    regItn.putExtra("flag",flag);
                    startActivity(regItn);
                }
                else {
                    Toast.makeText(VerifyActivity.this, "验证失败", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(ver);
    }

    /*检测网络*/
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
