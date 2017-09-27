package blackcat.hotspot.hotspot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.Random;

class DrawOnTop extends View {
    private static final String TAG = "DrawOnTop";

    CameraActivity ca = new CameraActivity();
    MainActivity ma = new MainActivity();
    private Context mContext;
    private GPSThreadV2 gp;

    private double mLat;
    private double mLng;

    public static int displaywidth=0;
    public static int displayheight=0;
    public static int width=0;
    public static int height=0;
    public static int picwidth=0;
    public static int picheight=0;

    private int srcrand=0;

    private Handler mHandler;
    public DrawOnTop(Context context, GPSThreadV2 gp) {
        this(context);
        this.gp = gp;
        Random rand = new Random();
        srcrand=rand.nextInt(5)+1;
        mHandler = new Handler();
        Log.d(TAG, "DrawOnTop() called with: " + "context = [" + context + "], gp = [" + gp + "]");

        gp.setOnLocationListener(new GPSThreadV2.OnLocationListener() {
            @Override
            public void onLocation(double lat, double lng) {
                Log.d(TAG, "onLocation() called with: " + "lat = [" + lat + "], lng = [" + lng + "]");
                mLat = lat;
                mLng = lng;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
            }
        });
    }

    public DrawOnTop(Context context) {
        super(context);
        mContext = context;
    }

    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw() called with: " + "canvas = [" + canvas + "]");
        super.onDraw(canvas);

        DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
        displaywidth = dm.widthPixels;
        displayheight = dm.heightPixels;
        Random rand = new Random();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap tmp=null;
        switch(srcrand)
        {
            case 1:
                tmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_incamera, options);
                break;
            case 2:
                tmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_incamera_tie, options);
                break;
            case 3:
                tmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_incamera_ring, options);
                break;
            case 4:
                tmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_incamera_ribbon, options);
                break;
            case 5:
                tmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_incamera_pink, options);
                break;
        }
        final Bitmap src=tmp;
        Log.d(TAG, "onDraw:\n\tdisplaywidth: " + displaywidth + " displayHeight: " + displayheight);


        picwidth = src.getWidth();
        picheight = src.getHeight();

        //final int width = rand.nextInt(displaywidth - src.getWidth());
        //final int height = rand.nextInt(displaywidth - src.getHeight());
        width = rand.nextInt(displaywidth - picwidth);
        height = rand.nextInt(displayheight - picheight);

        Log.d(TAG, "onDraw:\n\twidth: " + width + " height: " + height);
        Log.d(TAG, String.valueOf(mLat));
        for(int i=0;i<ma.gps_list.size();i++) {
            if (mLat > Double.parseDouble(ma.gps_list.get(i).get(0)) && mLat<Double.parseDouble(ma.gps_list.get(i).get(1)) && mLng>Double.parseDouble(ma.gps_list.get(i).get(2)) && mLng<Double.parseDouble(ma.gps_list.get(i).get(3))) {
                canvas.drawBitmap(src, width, height, null);
                Log.d(TAG, "canvas test");
                ca.gps_catch=srcrand;
            } else {
//            canvas.drawColor(Color.RED);
            }
        }
    }

    public int returnsize(int i)
    {
        switch (i)
        {
            case 1:return width;
            case 2:return height;
            case 3:return picwidth;
            case 4:return picheight;
        }
        return 0;
    }
}