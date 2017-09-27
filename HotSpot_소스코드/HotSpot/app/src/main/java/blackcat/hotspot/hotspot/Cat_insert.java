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

/**
 * Created by 현준 on 2016-10-30.
 */
public class Cat_insert extends AsyncTask<String, Void, Void> {
    String url = null;
    URL Url = null;
    MainActivity ma;
    CameraActivity ca;

    protected Void doInBackground(String... params) {
        String id = params[0];
        try{
            Url = new URL("http://182.209.141.134:40000/find.php?id="+id+"&catnum="+ca.gps_catch);  // URL화 한다.
            Log.d("url", String.valueOf(Url));
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성.
            conn.setRequestMethod("GET"); // get방식 통신
            conn.setDoOutput(true);       // 쓰기모드 지정
            conn.setDoInput(true);        // 읽기모드 지정
            conn.setUseCaches(false);     // 캐싱데이터를 받을지 안받을지
            conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

            InputStream is = conn.getInputStream();        //input스트림 개방

            StringBuilder builder = new StringBuilder();   //문자열을 담기 위한 객체
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));  //문자열 셋 세팅

            // while ((line = reader.readLine()) != null) {
            //     builder.append(line + "\n");
            // }
            String line=reader.readLine();
            //result = builder.toString();
            Log.d("test",line);
            conn.disconnect();
            is.close();


        } catch (MalformedURLException | ProtocolException exception) {
            exception.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }

        return null;
    }
}
