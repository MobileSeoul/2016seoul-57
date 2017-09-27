package blackcat.hotspot.hotspot;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

class Preview extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mHolder;
    Camera mCamera;
    CameraActivity ca = new CameraActivity();

    Preview(Context context) {
        super(context);

// SurfaceHolder.Callback을 설정함으로써 Surface가 생성/소멸되었음을 알 수 있습니다.
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();

//mCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;

        mCamera.setDisplayOrientation(0);
        try {
            mCamera.setPreviewDisplay(mHolder);
            Camera.Parameters parameters = mCamera.getParameters();

//아래 숫자를 변경하여 자신이 원하는 해상도로 변경한다
            if(parameters != null) {
                mCamera.setParameters(parameters);
            }
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

// 다른 화면으로 돌아가면, Surface가 소멸됩니다. 따라서 카메라의 Preview도

// 중지해야 합니다. 카메라는 공유할 수 있는 자원이 아니기에, 사용하지 않을

// 경우 -액티비티가 일시정지 상태가 된 경우 등 - 자원을 반환해야합니다.
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

// 표시할 영역의 크기를 알았으므로 해당 크기로 Preview를 시작합니다.
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
        Camera.Size size = previewSizeList.get(0);
        //Log.d("bbb", Integer.toString(size.width));
        //Log.d("bbb", Integer.toString(size.height));
        //parameters.setRotation(90);
        parameters.setPreviewSize(size.width, size.height);
        parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        parameters.setExposureCompensation(0);

        mCamera.setDisplayOrientation(90);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }
}