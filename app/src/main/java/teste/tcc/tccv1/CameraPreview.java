package teste.tcc.tccv1;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.ImageView;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;

/**
 * Created by AngeleLouise on 05/10/2016.
 */

public abstract class CameraPreview implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private Camera mCamera;
    private int plargura_tamanho;
    private int paltura_tamanho;
    private ImageView CameraPreview_2;
    private Bitmap bitmap;
    private int[] pixels = null;
    private int imageFormato;
    private boolean bProcessing = false;
    //private byte[] FrameData = null;
    Mat FrameData;
    Main_Show_Camera hist;

    Handler mHandler = new Handler(Looper.getMainLooper());

    public CameraPreview (int plargura, int paltura, ImageView CameraPreview){
        plargura_tamanho=plargura;
        paltura_tamanho=paltura;
        CameraPreview_2= CameraPreview;
        pixels = new int[ plargura_tamanho*paltura_tamanho];
        FrameData = new Mat(paltura, plargura, CvType.CV_8UC4);

    }
    public void onPreviewFrame (Mat FrameData, android.graphics.Camera arg1){

        //if (imageFormato == ImageFormat.NV21)
        //{
            //We only accept the NV21(YUV420) format.
            if ( !bProcessing )
                mHandler.post(DoImageProcessing);
            //}
    }

    public void onPause(){
        mCamera.stopPreview();
    }
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3){
    Parameters parameters;

    parameters = mCamera.getParameters();
    // Set the camera preview size
    parameters.setPreviewSize(plargura_tamanho, paltura_tamanho);

    imageFormato = parameters.getPreviewFormat();

    mCamera.setParameters(parameters);

    mCamera.startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0)
    {
        mCamera = Camera.open();
        try
        {
            // If did not set the SurfaceHolder, the preview area will be black.
            mCamera.setPreviewDisplay(arg0);
            mCamera.setPreviewCallback(this);
        }
        catch (IOException e)
        {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0)
    {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    /*public abstract void histogramas(int largura, int altura, byte[] NV21FrameData, int[] pixels);
    static
    {
        System.loadLibrary("histogramas");
    }*/

    private Runnable DoImageProcessing = new Runnable()
    {
        public void run()
        {
            Log.i("MyRealTimeImageProcessing", "DoImageProcessing():");
            bProcessing = true;
            //hist.onCameraFrame(plargura_tamanho,paltura_tamanho, FrameData, pixels);
            bitmap.setPixels(pixels, 0, plargura_tamanho, 0, 0, plargura_tamanho, paltura_tamanho);
            CameraPreview_2.setImageBitmap(bitmap);
            bProcessing = false;
        }
    };
}
