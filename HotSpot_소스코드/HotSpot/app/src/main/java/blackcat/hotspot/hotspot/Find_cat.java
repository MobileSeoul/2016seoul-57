package blackcat.hotspot.hotspot;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jayzi on 2016-10-31.
 */
public class Find_cat extends AsyncTask<String, Void, Void> {
    String url=null;
    URL Url=null;
    GalleryActivity ga;
    @Override
    protected Void doInBackground(String... params) {
        String id = params[0];
        url = "http://182.209.141.134:40000/user.php?id=" + id; //탐색하고 싶은 URL이다.
        Log.d("httphttp", url);
        try {
            Url = new URL(url);  // URL화 한다.
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성.
            conn.setRequestMethod("GET"); // get방식 통신
            conn.setDoOutput(true);       // 쓰기모드 지정
            conn.setDoInput(true);        // 읽기모드 지정
            conn.setUseCaches(false);     // 캐싱데이터를 받을지 안받을지
            conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

            //strCookie = conn.getHeaderField("Set-Cookie"); //쿠키데이터 보관

            InputStream is = conn.getInputStream();        //input스트림 개방

            StringBuilder builder = new StringBuilder();   //문자열을 담기 위한 객체
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));  //문자열 셋 세팅
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

            String result = builder.toString();
            Log.d("user cat", result + "a");
            conn.disconnect();

            ga.catlist=null;
            ga.catlist=new ArrayList<>();
            int point=0;
            for(int i=0;i<result.length();i++)
            {
                if(result.substring(i,i+1).equals(","))
                {
                    ga.catlist.add(result.substring(point,i));
                    point = i+1;
                }
            }

        } catch (MalformedURLException | ProtocolException exception) {
            exception.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return null;
    }
}
