package com.example.myq.mleaf;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

/*起始端；主要完成起始界面动画的加载*/
public class StartActivity extends Activity {

    private static final int LOAD_DISPLAY_TIME = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                StartActivity.this.finish();
            }
        }, LOAD_DISPLAY_TIME);
    }
}
