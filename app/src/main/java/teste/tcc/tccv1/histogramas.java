package teste.tcc.tccv1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static android.content.ContentValues.TAG;

/**
 * Created by AngeleLouise on 05/10/2016.
 */

public class histogramas  {

    Mat mat_alterada;
    Mat mRgba;

    public Mat filtro (Mat mRgba){
        mat_alterada = new Mat();
        mRgba = new Mat();
        this.mRgba = mRgba;
        Imgproc.blur(mRgba,mat_alterada,new Size(5,5));

        return mat_alterada;
    }


}
