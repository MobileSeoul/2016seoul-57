package blackcat.hotspot.hotspot;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Jayzi on 2016-10-31.
 */
public class PopupActivity extends Activity {
public static int position=0;
    public static String name=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.customdialog);

        TextView textview = (TextView)findViewById(R.id.tv_title);
        textview.setText(name);

        WebView webview = (WebView)findViewById(R.id.dialogweb1);
        WebView webview2 = (WebView)findViewById(R.id.dialogweb2);
        WebView webview3= (WebView)findViewById(R.id.dialogweb3);

        webview.loadUrl("http://182.209.141.134:40000/image_html/"+String.valueOf(position)+"/1.html");
        webview2.loadUrl("http://182.209.141.134:40000/image_html/"+String.valueOf(position)+"/2.html");
        webview3.loadUrl("http://182.209.141.134:40000/image_html/"+String.valueOf(position)+"/3.html");

        findViewById(R.id.btn_ok).setOnClickListener(mClickListener);
    }
    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //이곳에 버튼 클릭시 일어날 일을 적습니다.
            finish();
        }
    };
}
