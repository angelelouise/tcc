package teste.tcc.tccv1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
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
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.opencv.core.CvType.CV_8U;


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
    Mat hist0;
    Mat hist1;
    int width;
    int height;
    double limite = 0.95;
    // Variáveis para camera
    private int mCameraId =0;
    //variáveis para o histograma
    int hist_bins = 30;           //number of histogram bins
    //int hist_range[]= {0,180};//histogram range
    MatOfFloat ranges;
    MatOfInt histSize;
    boolean tag=false;
    double col;
    filtro1 hist = new filtro1();
    Menu tag_filtro = new Menu();
    private boolean bProcessing = false;
    Handler mHandler = new Handler(Looper.getMainLooper());
    boolean first =true;

//OpenCV Manager, ajuda o app a se comunicar com o android pra fazer o openCV funcionar
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView(); // essa variável é a ponte entre a camera e o openCV
                    ranges = new MatOfFloat( 0f,180f,0f,256f );
                    histSize = new MatOfInt(hist_bins,hist_bins);
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

        //um switch listenner tem que ser colocado no método oncreate
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
        hist0 = new Mat();
        hist1 = new Mat();
        this.width = width;
        this.height = height;

    }
//Destroi os dados da imagem quando vc para o preview da camera
    public void onCameraViewStopped() {
        mRgba.release();
        mGray.release();
        mRgba_anterior.release();
        hist0.release();
        hist1.release();
    }
//Deixa a camera melhor posicionada
    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        // TODO Auto-generated method stub
        mRgba = inputFrame.rgba();
        //inputFrame.copyTo(mRgba);
        mGray = inputFrame.gray();
        Mat borders = new Mat();
        /*na primeira vez que o programa roda a imagem anterior está vazia*/
        if(first){
            mRgba_anterior = mRgba.clone();
            first = false;
            Log.i("First processing", "teste");
        }

        col = comp_histogramas(hist0,hist1, mRgba, mRgba_anterior);
        if(col<limite){
            Imgproc.putText(mRgba,"ALTERACAO DE VIDEO", new Point(30,50),1, 3, new Scalar(255));
            tag=true;
            mHandler.post(DoImageProcessing);

        }
        else{
            tag=false;
        }
        mRgba_anterior = mRgba.clone();
        if(tag_filtro.tag_f==3){

            Mat gray = new Mat(height, width, CV_8U, new Scalar(255));
            //this.mRgba = mRgba;
            mRgba.channels();
            List<MatOfPoint> contornos = new ArrayList<MatOfPoint>();
            Mat hierarchy = new Mat();
            borders = new Mat(height, width, CV_8U, new Scalar(255));
            Mat points = new Mat(height, width, CV_8U, new Scalar(255));
            Imgproc.putText(mRgba,"filtro 2", new Point(30,50),1, 2, new Scalar(255));
            Imgproc.Canny(mGray, borders, 80, 100);
            //Imgproc.cvtColor(borders, gray, Imgproc.COLOR_RGBA2GRAY);
            Imgproc.findContours(borders, contornos, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

            /*for (int contourIdx = 0; contourIdx < contornos.size(); contourIdx++) {
                Imgproc.drawContours(mRgba, contornos, contourIdx, new Scalar(0, 0, 255), -1);
            }*/
            hierarchy.release();
            //desenhando os círculos
            for(int i = 0; i< contornos.size(); i++) {
                for (int j = 0; j< contornos.get(i).toArray().length; j++) {
                    Point[] aux= contornos.get(i).toArray();
                    double[] xgray = mRgba.get(i,j);
                    System.out.println(aux);
                    Imgproc.circle(points, new Point(aux[j].x, aux[j].y),1, new Scalar(0,255,0));
                }
            }
            return points;

        }
        return mRgba;
    }
    //função para comparar filtro1
    public double comp_histogramas (Mat hist0, Mat hist1, Mat mRgba, Mat mRgba_anterior){
        double correlacao;
        Imgproc.calcHist(Arrays.asList(mRgba), new MatOfInt(0,1), new Mat(), hist0, histSize, ranges);
        Imgproc.calcHist(Arrays.asList(mRgba_anterior), new MatOfInt(0,1), new Mat(), hist1, histSize, ranges);
        Core.normalize(hist0, hist0, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        Core.normalize(hist1, hist1, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        correlacao = Imgproc.compareHist(hist0, hist1, Imgproc.CV_COMP_CORREL);
        return correlacao;
    }
    /*função pra trocar de camera, mCameraId começa com valor inicial 0, cada vez que o switch é usado seus valores são
    * trocados entre 1 e 0
    * 0 é o modo OFF que é a back
    * 1 é o modo ON que é o front*/
    public void swap (){
        mCameraId = mCameraId^1; //troca de 1 pra 0 e vice-versa;
        mOpenCvCameraView.disableView();
        mOpenCvCameraView.setCameraIndex(mCameraId);
        mOpenCvCameraView.enableView();
    }
    /*Função para selecionar os filtros que vão na thread*/
    public Mat Mythread(Mat mRgba, Mat mGray, int height, int width){
        if(tag_filtro.tag_f==1) {
            return hist.filtro(mRgba);
            //return hist.pontilhismo(mRgba,mGray, height, width);
        }
        if(tag_filtro.tag_f==2) {
            return hist.pontilhismo(mRgba, mGray, height, width);
        }

        return mRgba;
    }
    public void onPreviewFrame (Mat FrameData, android.graphics.Camera arg1){

        //if (imageFormato == ImageFormat.NV21)
        //{
        //We only accept the NV21(YUV420) format.
        if ( !bProcessing )
            mHandler.post(DoImageProcessing);
        //}
    }
    private Runnable DoImageProcessing = new Runnable()
    {
        public void run()
        {
            Log.i("RealTimeImageProcessing", "DoImageProcessing():");
            //bProcessing = true;
            mRgba = Mythread(mRgba, mGray, height, width);
            //bProcessing = false;
        }
    };

}
