package blackcat.hotspot.hotspot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

/**
 * Created by HyeonJun on 2016-08-14.
 */
public class SplashActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d("splash titlebar", "titlebar success");
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            public void run() {
               exit();
            }
        }, 2000);
    }
    public void exit()
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
