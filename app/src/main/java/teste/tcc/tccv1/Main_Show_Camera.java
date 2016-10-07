package teste.tcc.tccv1;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

// OpenCV classes
import org.opencv.android.JavaCameraView;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;


public class Main_Show_Camera extends AppCompatActivity implements CvCameraViewListener2{
    // Used for logging success or failure messages
    private static final String TAG = "OCVSample::Activity";

    // Loads camera view of OpenCV for us to use. This lets us see using OpenCV
    private CameraBridgeViewBase mOpenCvCameraView;

    // These variables are used (at the moment) to fix camera orientation from 270degree to 0degree
    Mat mRgba;
    Mat mRgbaF;
    Mat mRgbaT;
    Mat mGray; //facilitar o calculo do programa
    Mat mRgba_anterior;
    int width;
    int height;
    double limite = 0.97;
    // Variáveis para camera
    private int mCameraId =0;

//OpenCV Manager, ajuda o app a se comunicar com o android pra fazer o openCV funcionar
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView(); // essa variável é a ponte entre a camera e o openCV
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    //Criar a atividade
    public Main_Show_Camera() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }
    //Display a camera do openCV no layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        //Esconde o título do app
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.show_camera);

        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.show_camera_activity_java_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);
        //mOpenCvCameraView.setMaxFrameSize(1000, 800);

        //um switch listenner
        Switch swap = (Switch) findViewById(R.id.swap);
        swap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    swap();
                }
                else{
                    swap();
                }
            }
        });
    }
    //handlers para os eventos de pausa, resume, closed/destroyed
    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
//Recebe os dados da imagem quando o preview da camera aparece na tela
    public void onCameraViewStarted(int width, int height) {
        /*mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);*/
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgba_anterior = new Mat(height, width, CvType.CV_8UC4);
        this.width = width;
        this.height = height;

    }
//Destroi os dados da imagem quando vc para o preview da camera
    public void onCameraViewStopped() {
        mRgba.release();
        mGray.release();
    }
//Deixa a camera melhor posicionada
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        // TODO Auto-generated method stub
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        // Rotate mRgba 90 degrees
        //Core.transpose(mRgba, mRgbaT);
        //Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0,0, 0);
        //Core.flip(mRgbaF, mRgba, 1 );
        if(comp_histogramas(mRgba,mRgba_anterior)< limite){
           EditText teste = (EditText) findViewById(R.id.teste);
           teste.setText("Mexeu", TextView.BufferType.EDITABLE);
        }

        mRgba_anterior = mRgba.clone();


        return mRgba; // This function must return
    }
    public double comp_histogramas (Mat mRgba, Mat mRgba_anterior){
        double correlacao;
        correlacao = Imgproc.compareHist(mRgba, mRgba_anterior, Imgproc.CV_COMP_CORREL);
        return correlacao;
    }
    //função pra trocar de camera
    public void swap (){
        mCameraId = mCameraId^1; //troca de 1 pra 0 e vice-versa;
        mOpenCvCameraView.disableView();
        mOpenCvCameraView.setCameraIndex(mCameraId);
        mOpenCvCameraView.enableView();
    }

}
