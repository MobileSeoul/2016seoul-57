package blackcat.hotspot.hotspot;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by HyeonJun on 2016-08-15.
 */
public class CameraActivity extends Activity {
    private Preview mPreview;
    private Activity context;
    private Handler mHandler;
    public static int chk=2;
    private GPSThreadV2 gp;
    public static ProgressDialog dialog= null;
    static double lat=0;
    static double lng=0;
    private String Tag="Log Tag";
    private DrawOnTop dt;
    public static int gps_catch=0;
    public static int checkcat=0;

    ViewGroup menu1;
    ViewGroup menu2;
    ViewGroup menu3;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(this);
        menu1 = (ViewGroup) findViewById(R.id.main);
        menu2 = (ViewGroup) findViewById(R.id.camera);
        menu3 = (ViewGroup) findViewById(R.id.picture);
    }

    public void onDestroy()
    {
        super.onDestroy();
        Log.d("test", "destroy");
    }

    public void onBackPressed() {
        Log.d("Testtestlog", "aa");
        if(gp.isAlive()) {
            stop();
        }
        /*if(chk==0)
        {
            dialog.dismiss();
        }*/
        finish();
    }
    public void create(Activity activity) {
        Log.d("Start", "aa");
        context=activity;
        //startActivityForResult(new Intent(this, SplashActivity.class), 0);

        //startActivity(new Intent(this, GPSActivity.class));
        // Hide the window title.
        handler.run();
        // Create our Preview view and set it as the content of our activity.
        gp = new GPSThreadV2(context, mHandler);
        gp.setDaemon(true);
        gp.start();
        Log.d("gps start","A");

        //dialog = ProgressDialog.show(CameraActivity.this, "", "로딩 중입니다. 잠시 기다려주세요", true);
    }

    public void stop()
    {
        gp.stopUsingGPS();
        gp.interrupt();
        Log.d("gps interupt","A");
        chk=0;
        //mHandler.removeMessages(0);
    }
    public void resume()
    {
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_UP :    //화면을 터치했다 땠을때
                Log.d(Tag,"motion Event");
                Log.d(Tag,String.valueOf(event.getX()));
                if(event.getX()>dt.width && event.getX()<dt.width+dt.picwidth && event.getY()>dt.height && event.getY()<dt.height+dt.picheight)
                {
                    Log.d(Tag, "touch!");
                    Toast toast = Toast.makeText(this, "깜냥이를 찾았다!\n찾은 깜냥이는 갤러리에 등록됩니다.\n갤러리 항목에서 확인해주세요",Toast.LENGTH_SHORT);
                    toast.show();
                    TelephonyManager telephony = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    String phone_deviceid = telephony.getDeviceId();
                    Cat_Isindb cd = new Cat_Isindb();
                    try {
                        cd.execute(phone_deviceid).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if(checkcat==0) {
                        Cat_insert ci = new Cat_insert();
                        ci.execute(phone_deviceid);
                    }
                    if(gp.isAlive()) {
                        stop();
                    }
                    finish();

                }
                break;

        }
        return super.onTouchEvent(event);
    }

    public Runnable handler = new Runnable() {
        public void run()
        {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.obj != null) {   // Message id 가 0 이면
                        if (chk == 0) {
                            Log.d("cat test","aaaaa");
                            dialog.dismiss();
                            mPreview = new Preview(context);
                            context.setContentView(mPreview);

                            draw.run();
                            chk = 1;
                            // 메인스레드의 UI 내용 변경
                        }
                    }
                }
            };
        }
    };

    public Runnable draw = new Runnable()
    {
        @Override
        public void run()
        {
            Log.d("draw test","aaaaa");
            DrawOnTop mDraw = new DrawOnTop(context, gp);
            context.addContentView(mDraw, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //while(lat>0)
            //{
            mDraw=new DrawOnTop(context);
            //}

        }
    };
}
