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

public class histogramas  {

    Mat mat_alterada;

    protected Mat filtro (Mat mRgba){
        Imgproc.bilateralFilter(mRgba,mat_alterada,1,2,0.5,1);
       return mat_alterada;
    }


}
