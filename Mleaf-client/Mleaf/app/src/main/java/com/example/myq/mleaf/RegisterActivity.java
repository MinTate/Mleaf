package com.example.myq.mleaf;

import com.example.myq.service.LoginService;
import com.example.myq.service.RegisterService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;

/**
 * Created by MYQ on 2016/3/7.
 */
public class RegisterActivity  extends Activity implements View.OnClickListener{

    // 登陆按钮
    private Button register;
    // 显示用户名和密码
    EditText password, prepassword;
    //显示标题提示栏
    TextView title_tip;
    // 创建等待框
    private ProgressDialog dialog;
    // 返回的数据
    private String info,phonenumber;
    private String flag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);

        // 获取控件
        password = (EditText) findViewById(R.id.password);
        prepassword = (EditText) findViewById(R.id.prepassword);
        register = (Button) findViewById(R.id.register);
        title_tip=(TextView)findViewById(R.id.title_tip);

        //从VerifyActivity获取传递过来的phonenumber值
        Intent intent=getIntent();
        phonenumber=intent.getStringExtra("phonenumber");
        flag=intent.getStringExtra("flag");

        //通过判断flag的值 来确定标题提示
        if(flag.equals("1")==true){
            title_tip.setText("注册");
        }
        if(flag.equals("2")==true){
            title_tip.setText("找回密码");
        }


        // 设置按钮监听器
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        // 检测网络，无法检测wifi
        if (!checkNetwork()) {
            Toast toast = Toast.makeText(RegisterActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else {
            /*判定密码框和输入密码框是否为空，如果不为空 跳转 否则产生提示*/
            if(password.getText().toString().equals("")==false&&prepassword.getText().toString().equals("")==false){
                if (password.getText().toString().equals(prepassword.getText().toString())){
                    // 提示框
                    dialog = new ProgressDialog(this);
                    dialog.setTitle("提示");
                    dialog.setMessage("正在注册...");
                    dialog.setCancelable(false);
                    dialog.show();

                    ReadNet(phonenumber, password.getText().toString());
                }else{
                    Toast.makeText(RegisterActivity.this, "密码不一致", Toast.LENGTH_LONG).show();
                }
            }else{
                if(prepassword.getText().toString().equals("")){

                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_LONG).show();

                }else{

                    Toast.makeText(RegisterActivity.this, "请确认密码", Toast.LENGTH_LONG).show();

                }
            }



        }
    }

    //登陆验证模块
    public void ReadNet(String user,String pass){

        new AsyncTask<String,Void,String>(){

            @Override
            protected String doInBackground(String... params) {
                return  RegisterService.executeHttpPost(params[0],params[1]);
            }

            protected void onPostExecute(String result){
                if(result.equals("true")){
                    //通过判断flag的值 来确定标题提示
                    if(flag.equals("1")==true){
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                    }
                    if(flag.equals("2")==true){
                        Toast.makeText(RegisterActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                    }

                    dialog.dismiss();
                }
                else {
                    //通过判断flag的值 来确定标题提示
                    if(flag.equals("1")==true){
                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                    }
                    if(flag.equals("2")==true){
                        Toast.makeText(RegisterActivity.this, "修改失败", Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }
            }
        }.execute(user,pass);
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

