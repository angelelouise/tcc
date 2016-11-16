package teste.tcc.tccv1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.ContentValues.TAG;
import static org.opencv.core.CvType.CV_8U;

/**
 * Created by AngeleLouise on 05/10/2016.
 */

public class filtro1 {

    Mat mat_alterada;
    Mat mRgba;
    Mat hierarchy;
    List<MatOfPoint> contours;
    private Mat mIntermediateMat;

    public Mat filtro (Mat mRgba){
        mat_alterada = new Mat();
        mRgba = new Mat();
        this.mRgba = mRgba;
        Imgproc.blur(mRgba,mat_alterada,new Size(5,5));
        Imgproc.putText(mRgba,"thread ok", new Point(30,50),1, 2, new Scalar(255));
        return mat_alterada;
    }
    public Mat pontilhismo (Mat mRgba, int height, int width){
        mRgba = new Mat();
        char gray;
        this.mRgba = mRgba;
        contours = new ArrayList<MatOfPoint>();
        hierarchy = new Mat();
        Mat points = new Mat(height, width, CV_8U, new Scalar(255));
        int x,y;

        Imgproc.Canny(mRgba, mIntermediateMat, 80, 100);
        Imgproc.findContours(mIntermediateMat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
        //desenhando os círculos
        for(int i=0; i<contours.size(); i++) {
            for (int j=0; j<contours.get(i).rows(); j++) {
                gray = mRgba.get(contours.get(i).get(j).y, contours.get(i).get(j).x)[0];
                Imgproc.circle(points, Point(contours.get(i).get(j).x, contours.get(i).get(j).y),1, new Scalar(gray, gray, gray));
            }
        }



        hierarchy.release();
        return mRgba;
    }


}
