package blackcat.hotspot.hotspot;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.ViewGroup;

public class GPSThreadV2 extends Thread {
    public interface OnLocationListener {
        void onLocation(double lat, double lng);
    }
    private OnLocationListener listener;

    CameraActivity ca = new CameraActivity();
    private int messagecount = 0;
    private final Context mContext;
    // 현재 GPS 사용유무
    boolean isGPSEnabled = false;
    // 네트워크 사용유무
    boolean isNetworkEnabled = false;
    // GPS 상태값

    Handler mMainHandler;
    public int chk = 0;
    // 최소 GPS 정보 업데이트 거리 10미터

    protected LocationManager locationManager;
    LocationListener locationListener;

    public GPSThreadV2(Context context, Handler handler) {
        mContext = context;
        mMainHandler = handler;
    }

    public void run() {
        Looper.prepare();
        getLocation();
        Looper.loop();
    }

    public void setOnLocationListener(OnLocationListener listener) {
        this.listener = listener;
    }

    public void getLocation() {
        try {

            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            // GPS 정보 가져오기
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // 현재 네트워크 상태 값 알아오기
            //isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.d(String.valueOf(isGPSEnabled), "aaaaaaaaaaaaaaa");
            if (!isGPSEnabled) {
                Log.d("enabled", "testtest");
                // GPS 와 네트워크사용이 가능하지 않을때 소스 구현
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

                alertDialog.setTitle("GPS Setting");
                alertDialog.setMessage("GPS 사용설정이 되어있지 않습니다.\n설정창으로 가시겠습니까?");
                // OK 를 누르게 되면 설정창으로 이동합니다.
                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                        ((Activity)mContext).finish();
                    }
                });
                // Cancle 하면 종료 합니다.
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ((Activity)mContext).finish();

                    }
                });
                alertDialog.show().setCanceledOnTouchOutside(false);
            } else {

                ca.dialog = ProgressDialog.show(mContext, "", "로딩 중입니다. 잠시 기다려주세요", true);
                ca.chk=0;
                locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();
                        Log.d("LOG", "latitude: " + lat + ", longitude: " + lng);

                        if (listener != null)
                            listener.onLocation(lat, lng);

                        if(chk==0)
                        {
                            chk++;
                            Message msg = new Message();
                            msg = mMainHandler.obtainMessage();
                            msg.obj = lat + " , " + lng;
                            mMainHandler.sendMessage(msg);
                        }
                        ca.lat = lat;
                        ca.lng = lng;
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Log.d("LOG", "onStatusChanged");
                    }

                    public void onProviderEnabled(String provider) {
                        Log.d("LOG", "onProviderEnabled");
                    }

                    public void onProviderDisabled(String provider) {
                        Log.d("LOG", "onProviderDisabled");
                    }
                };
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2500, 0, locationListener);

                /*this.isGetLocation = true;
                // 네트워크 정보로 부터 위치값 가져오기
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            //location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                // 위도 경도 저장
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                                Log.d(String.valueOf(lat),"lat_network");
                                if(messagecount==0)
                                {
                                    Message msg = new Message();
                                    msg = mMainHandler.obtainMessage();
                                    msg.obj = lat + " , " + lon;
                                    mMainHandler.sendMessage(msg);
                                    messagecount=1;
                                }
                            }
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                                Log.d(String.valueOf(lat),"lat_gps");
                                if(messagecount==0)
                                {
                                    Message msg = new Message();
                                    msg = mMainHandler.obtainMessage();
                                    msg.obj = lat + " , " + lon;
                                    mMainHandler.sendMessage(msg);
                                    messagecount=1;
                                }
                            }
                        }
                    }
                }
                stopchk=1;
            }*/

            }
            //return location;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * GPS 종료
     * */
    public void stopUsingGPS() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(locationListener);
        }
    }
}