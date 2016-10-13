package teste.tcc.tccv1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import static android.content.ContentValues.TAG;

/**
 * Created by AngeleLouise on 05/10/2016.
 */

public class histogramas extends AppCompatActivity {
    Mat mRgba;
    Mat mRgba_anterior;
    boolean tag=false;
    double limite=0.997;
    //Core.split(mRgba, hsv_planes);
    //Imgproc.calcHist(Arrays.asList(mRgba), new MatOfInt(0), new Mat(), mRgba, histSize, ranges);
    //Imgproc.calcHist(Arrays.asList(mRgba_anterior), new MatOfInt(0), new Mat(), mRgba_anterior, histSize, ranges);

    //correlacao = Imgproc.compareHist(mRgba, mRgba_anterior, Imgproc.CV_COMP_CORREL);
    //filtro_hist(correlacao);

    //mRgba_anterior = mRgba;
    public histogramas() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
    }
    public double comp_histogramas (Mat mRgba, Mat mRgba_anterior){
        this.mRgba = mRgba;
        this.mRgba_anterior = mRgba_anterior;
        double correlacao;
        correlacao = Imgproc.compareHist(mRgba, mRgba_anterior, Imgproc.CV_COMP_CORREL);
        return correlacao;
    }
    public boolean tag (){
        if(comp_histogramas(mRgba,mRgba_anterior)< limite){
            tag=true;
        }
        return tag;
    }
}
