package com.example.myq.mleaf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.View.OnClickListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myq.service.LoginService;
import com.example.myq.service.SyncHttp;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
* 调用系统相机相册+结果返回界面
* @author: MYQ
* @data: 2016/2/28
* */

public class LeafActivity extends Activity implements OnClickListener {
   // private static int RESULT_LOAD_IMAGE = 1;
    private TextView name;
    private TextView description;
    private ImageView imageframe;

    //用于结果反馈的三个按钮
    private ImageButton leaf_right;
    private ImageButton leaf_indistinct;
    private ImageButton leaf_error;

    // 创建等待框
    private ProgressDialog dialog;
    private HttpUtils httpUtils;
    private String URL="http://111.114.116.28:8080/Mleaf/LeafServlet";

    private String leaf_category="";

    private static final int PHOTO_PICK = 2;
    private static final int PHOTO_CUT = 3;
    // 创建一个以当前系统时间为名称的文件，防止重复
    private File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(date) + ".png";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaf);
        name= (TextView) findViewById(R.id.name);
        description= (TextView) findViewById(R.id.description);
        imageframe=(ImageView)findViewById(R.id.imageframe);

        leaf_right= (ImageButton) findViewById(R.id.leaf_right);
        leaf_indistinct= (ImageButton) findViewById(R.id.leaf_indistinct);
        leaf_error= (ImageButton) findViewById(R.id.leaf_error);

        leaf_right.setEnabled(false);
        leaf_indistinct.setEnabled(false);
        leaf_error.setEnabled(false);

         /*添加按钮点击事件*/
        leaf_right.setOnClickListener(this);
        leaf_indistinct.setOnClickListener(this);
        leaf_error.setOnClickListener(this);

        httpUtils=new HttpUtils(10000);

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_PICK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_PICK:
                if (null != data) {
                    startPhotoZoom(data.getData(), 300);
                }
                break;
            case PHOTO_CUT:
                if (null != data) {
                    setPicToView(data);
                    // 提示框
                    dialog = new ProgressDialog(this);
                    dialog.setTitle("提示");
                    dialog.setMessage("正在识别，请稍后...");
                    dialog.setCancelable(false);
                    dialog.show();
                    upload();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 调用系统裁剪
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以裁剪
        intent.putExtra("crop", true);
        // aspectX,aspectY是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY是裁剪图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        // 设置是否返回数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CUT);
    }
    // 将裁剪后的图片显示在ImageView上
    private void setPicToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (null != bundle) {
            final Bitmap bmp = bundle.getParcelable("data");
            imageframe .setImageBitmap(bmp);
            saveCropPic(bmp);
            Log.i("MainActivity", tempFile.getAbsolutePath());
        }
    }

    // 上传文件到服务器
    protected void upload() {
        RequestParams params=new RequestParams();
        params.addBodyParameter(tempFile.getPath().replace("/", ""), tempFile);
        httpUtils.send(HttpRequest.HttpMethod.POST, URL, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException e, String msg) {
                Toast.makeText(LeafActivity.this, "上传失败，检查一下服务器地址是否正确", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", e.getExceptionCode() + "====="
                        + msg);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Toast.makeText(LeafActivity.this, "上传成功，马上去服务器看看吧！", Toast.LENGTH_SHORT).show();
                description.setText(responseInfo.result);
                String[] des = responseInfo.result.split("\\|");
                des[0] = des[0].replace("null", "");
                des[0] = des[0].replace(" ", "");
                leaf_category = des[0];
                name.setText(des[0]);
                String s = new String();
                for (int i = 1; i < des.length; i++) {
                    s = s + des[i] + "\n\n";
                }
                //将返回的植物种类概述数据显示在textview中
                description.setMovementMethod(ScrollingMovementMethod.getInstance());
                description.setText(s);

                //使按钮浮现
                leaf_right.setEnabled(true);
                leaf_indistinct.setEnabled(true);
                leaf_error.setEnabled(true);


                Log.i("MainActivity", "====upload_error====="
                        + responseInfo.result);
                dialog.dismiss();
                ;
            }
        });
    }

    // 把裁剪后的图片保存到sdcard上
    private void saveCropPic(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fis = null;
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try {
            fis = new FileOutputStream(tempFile);
            fis.write(baos.toByteArray());
            fis.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != baos) {
                    baos.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leaf_right:
                ReadNet("right");
                break;
            case R.id.leaf_indistinct:
                Toast.makeText(LeafActivity.this, "亲，可以百度一下啊~", Toast.LENGTH_LONG).show();
                break;
            case R.id.leaf_error:
                ReadNet("error");
                break;
            default:
                break;
        }
    }


    //登陆验证模块
    public void ReadNet(String judge){

        new AsyncTask<String,Void,String>(){

            @Override
            protected String doInBackground(String... params) {
                return  Assess(params[0]);
            }

            protected void onPostExecute(String result){
                if(result.equals("true")){
                    Toast.makeText(LeafActivity.this, "评价成功", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(LeafActivity.this,"评价失败", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(judge);
    }

    /**
     * 获取新闻详细信息
     *
     * @return
     */
    private String Assess(String assess)
    {
        String retStr = "";
        SyncHttp syncHttp = new SyncHttp();
        String params = "assess="+assess+"&result="+leaf_category+"&fileName="+tempFile.getName();
       // String params = "assess="+assess+"&result="+leaf_category+"&fileName="+tempFile.getName();
        String url="http://111.114.116.28:8080/Mleaf/SaveResult";
        try
        {
            retStr = syncHttp.httpGet(url, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retStr;
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
