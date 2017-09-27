package blackcat.hotspot.hotspot;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by HyeonJun on 2016-08-17.
 */

public class GPSThread extends Thread
{
    Context context;
    LocationManager manager;
    Handler mMainHandler;
    boolean network_enabled = false;

    public GPSThread (Context c, Handler handler)
    {
        this.context = c;
        mMainHandler = handler;
    }
    @Override
    public void run()
    {
        //super.run();
        Looper.prepare();
        getMyLocation();
        Looper.loop();

    }

    private void getMyLocation() {
        if (manager == null) {
            manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        network_enabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        // provider 기지국||GPS 를 통해서 받을건지 알려주는 Stirng 변수
        // minTime 최소한 얼마만의 시간이 흐른후 위치정보를 받을건지 시간간격을 설정 설정하는 변수
        // minDistance 얼마만의 거리가 떨어지면 위치정보를 받을건지 설정하는 변수
        // manager.requestLocationUpdates(provider, minTime, minDistance, listener);

        long minTime = 1000;

        // 거리는 0으로 설정
        // 그래서 시간과 거리 변수만 보면 움직이지않고 10초뒤에 다시 위치정보를 받는다
        float minDistance = 0;

        MyLocationListener listener = new MyLocationListener();
        //퍼미션이 제대로 설정되었는 지 체크
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        Log.d("aaaa", "testtest");
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, 0, listener);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, 0, listener);
    }

    class MyLocationListener implements LocationListener {
            // 위치정보는 아래 메서드를 통해서 전달된다.
            Message msg = new Message();
            int chk=0;
            @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d(String.valueOf(latitude), "latitude");
            if(chk==0) {
                msg = mMainHandler.obtainMessage();
                msg.obj = latitude + " , " + longitude;
                mMainHandler.sendMessage(msg);
                chk=1;
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            gps_off(provider);
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void gps_off(String provider)
        {
            //Toast.makeText(context, provider + "에 의한 위치서비스가 꺼져 있습니다. 켜주시기 바랍니다.", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            alertDialog.setTitle("GPS Setting");
            alertDialog.setMessage("정확도를 위해 GPS와 데이터를 모두사용하여 위치정보를 사용하고자 합니다.\n설정창으로 가시겠습니까?");
            // OK 를 누르게 되면 설정창으로 이동합니다.
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            // Cancle 하면 종료 합니다.
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alertDialog.show();
        }
        //System.exit(-1);
    }
}
