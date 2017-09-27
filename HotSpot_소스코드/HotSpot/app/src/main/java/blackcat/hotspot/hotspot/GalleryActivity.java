package blackcat.hotspot.hotspot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GalleryActivity extends Activity {
    CameraActivity ca;
    private long backKeyPressed=0;
    private Toast toast;
    private int height=0;

    ArrayList<Listviewitem2> datas= new ArrayList<Listviewitem2>();
    public static ArrayList<String> catlist = new ArrayList<>();
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_gallery);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        ImageButton button = (ImageButton) findViewById(R.id.btnMap);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // LocationManager 객체 초기화 , LocationListener 리스너 설정
                AlertDialog.Builder alert = new AlertDialog.Builder(GalleryActivity.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                alert.setMessage("버전 : version 1.0\n\nMade by : Team. 깜냥 (대표 권현준)\n\n개발 관련 문의\n   -> hjkwon0123@naver.com\n\n사진 명소 추천\n   -> hjkwon0123@naver.com\n\nThank you for download our APP");
                alert.show();
            }
        });

        final ViewGroup menu1 = (ViewGroup) findViewById(R.id.main);
        final ViewGroup menu2 = (ViewGroup) findViewById(R.id.camera);
        final ViewGroup menu3 = (ViewGroup) findViewById(R.id.picture);

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryActivity.this,MainActivity.class));
                overridePendingTransition(0,0);
                finish();
            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryActivity.this, CameraActivity.class));
            }
        });
        TelephonyManager telephony = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String phone_deviceid = telephony.getDeviceId();    //device id
        Find_cat fc = new Find_cat();
        try {
            fc.execute(phone_deviceid).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(int i=0;i<catlist.size();i++)
        {
            switch (catlist.get(i))
            {
                case "1": datas.add( new Listviewitem2("깜냥", "까만고양이파 두목, HotSpot의 메인모델을 맡고있다", R.drawable.logo_incamera));break;
                case "2": datas.add( new Listviewitem2("타이냥", "나비넥타이를 맨 까만고양이, 현재 코난의 수제자로 활약중에 있다", R.drawable.logo_incamera_tie));break;
                case "3": datas.add( new Listviewitem2("방울냥", "언제나 방울을 달고 다니는 까만고양이, 방울을 왜 달고다니는지는 알 수 없다", R.drawable.logo_incamera_ring));break;
                case "4": datas.add( new Listviewitem2("리본냥", "리본을 꼬리에 묶고 다니는 까만고양이, 자신이 이쁘다고 착각에 빠져있다", R.drawable.logo_incamera_ribbon));break;
                case "5": datas.add( new Listviewitem2("핑크냥", "깜냥이 첫눈에 반해 세번의 고백끝에 사귀게 된 여자친구! 시크함이 매력이다", R.drawable.logo_incamera_pink));break;
            }
            //datas.add( new Listviewitem2("Test", "위치", R.drawable.logo_incamera));
        }
        //datas.add( new Listviewitem2("Test", "위치", R.drawable.logo_incamera));
        //datas.add( new Listviewitem2("Test", "위치", R.drawable.logo_incamera));
        //datas.add( new Listviewitem2("Test", "위치", R.drawable.logo_incamera));
        //datas.add( new Listviewitem2("Test", "위치", R.drawable.logo_incamera));

        //ListView 객체 찾아와서 참조
        listview= (ListView)findViewById(R.id.listview);
        ListviewAdapter2 adapter= new ListviewAdapter2( getLayoutInflater() , datas);
        //위에 만든 Adapter 객체를 AdapterView의 일종인 ListView에 설정.
        listview.setAdapter(adapter);
    }
    private int getStatusBarSize() {
        Rect rectgle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
        int StatusBarHeight = rectgle.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int TitleBarHeight = contentViewTop - StatusBarHeight;

        return TitleBarHeight;
        //Log.i("getHeight", "StatusBar Height= " + StatusBarHeight + " TitleBar Height = " + TitleBarHeight);
    }
    public void onBackPressed() {
        if(System.currentTimeMillis()>backKeyPressed+2000)
        {
            backKeyPressed=System.currentTimeMillis();
            toast = Toast.makeText(this,"\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            toast.cancel();
            finish();
        }
    }

    public void onPause(){
        super.onPause();
        Log.i("titlebar", "onPause()");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.i("titlebar", "onStop()");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i("titlebar", "onResume()");
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.i("titlebar", "onStart()");
    }

    @Override
    public void onRestart(){
        super.onRestart();
        Log.i("titlebar", "onRestart()");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("titlebar", "onDestroy()");
    }
}

