package blackcat.hotspot.hotspot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {
    CameraActivity ca;
    private long backKeyPressed=0;
    private Toast toast;
    //private ListView m_ListView;
    ArrayList<Listviewitem> datas= new ArrayList<Listviewitem>();
    ListView listview;

    private String phone_deviceid=null;
    public static int count=0;
    public static ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
    public static ArrayList<ArrayList<String>> gps_list = new ArrayList<ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        ImageButton button = (ImageButton) findViewById(R.id.btnMap);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // LocationManager 객체 초기화 , LocationListener 리스너 설정
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
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

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
            }
        });

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
                overridePendingTransition(0,0);
                finish();
            }
        });

        TelephonyManager telephony = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        phone_deviceid = telephony.getDeviceId();    //device id

        Count count=new Count();
        try {
            count.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Location location=new Location();
        try {
            location.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Server_request sr = new Server_request();
        //sr.execute("1",phone_deviceid);


        Log.d("location", String.valueOf(list.size()));
        ArrayList<String> tmp = new ArrayList<>();
        for(int i=0;i<list.size();i++)
        {
            datas.add( new Listviewitem(list.get(i).get(1), list.get(i).get(2), "http://182.209.141.134:40000/image_html/"+list.get(i).get(7)+".html"));
            tmp.add(list.get(i).get(3));
            tmp.add(list.get(i).get(4));
            tmp.add(list.get(i).get(5));
            tmp.add(list.get(i).get(6));
            gps_list.add(tmp);
            tmp=null;
            tmp=new ArrayList<>();
        }
        //datas.add( new Listviewitem("Test", "위치", "http://182.209.141.134:40000/image_html/1.html"));
        //datas.add( new Listviewitem("Test", "위치", "http://182.209.141.134:40000/image_html/1.html"));
        //datas.add( new Listviewitem("Test", "위치", "http://182.209.141.134:40000/image_html/1.html"));
        //datas.add( new Listviewitem("Test", "위치", "http://182.209.141.134:40000/image_html/1.html"));
        //datas.add( new Listviewitem("Test", "위치", "http://182.209.141.134:40000/image_html/1.html"));

        //ListView 객체 찾아와서 참조
        listview= (ListView)findViewById(R.id.listview);

        //AdapterView의 일종인 ListView에 적용할 Adapter 객체 생성
        //MemberData 객체의 정보들(이름, 국적, 이미지)를 적절하게 보여줄 뷰로 만들어주는 Adapter클래스 객체생성
        //이 예제에서는 MemberDataAdapter.java 파일로 클래스를 설계하였음.
        //첫번재 파라미터로 xml 레이아웃 파일을 객체로 만들어 주는 LayoutInflater 객체 얻어와서 전달..
        //두번째 파라미터는 우리가 나열한 Data 배열..
        ListviewAdapter adapter= new ListviewAdapter( getLayoutInflater() , datas);
        //위에 만든 Adapter 객체를 AdapterView의 일종인 ListView에 설정.
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                TextView a = (TextView)view.findViewById(R.id.text_name);
                new PopupActivity().name=a.getText().toString();
                Log.d("click!",String.valueOf(position));
                new PopupActivity().position=position+1;
                Intent intent_ = new Intent(MainActivity.this, PopupActivity.class);
                intent_.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);   // 이거 안해주면 안됨
                MainActivity.this.startActivity(intent_);
            }
        });
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

    public void locationcheck(ArrayList<ArrayList<String>> tmp)
    {
        list = tmp;
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

