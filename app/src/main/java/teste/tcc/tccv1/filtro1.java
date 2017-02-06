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
    Mat mGray;
    Mat hierarchy;
    List<MatOfPoint> contours;
    private Mat borders;

    public Mat filtro (Mat mRgba){
        mat_alterada = new Mat();
        this.mRgba = mRgba;
        //Imgproc.blur(mRgba,mat_alterada,new Size(5,5));
        Imgproc.cvtColor(mRgba, mat_alterada, Imgproc.COLOR_RGBA2GRAY);
        //Imgproc.putText(mRgba,"filtro 1", new Point(30,50),1, 2, new Scalar(255));
        return mat_alterada;
    }
    public Mat pontilhismo (Mat mRgba, Mat mGray, int height, int width){
        this.mRgba = mRgba;
        this.mGray = mRgba;
        contours = new ArrayList<MatOfPoint>();
        hierarchy = new Mat();
        borders = new Mat(height, width, CV_8U, new Scalar(255));
        Mat points = new Mat(height, width, CV_8U, new Scalar(255));

        Imgproc.putText(mRgba,"filtro 2", new Point(30,50),1, 2, new Scalar(255));
        Imgproc.Canny(mGray, borders, 80, 100);
        //Imgproc.cvtColor(borders, gray, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.findContours(borders, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        //desenhando os círculos
        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            Imgproc.drawContours(mRgba, contours, contourIdx, new Scalar(0, 0, 255), -1);
        }
        hierarchy.release();
        /*for(int i=0; i<contours.size(); i++) {
            for (int j=0; j<contours.get(i).rows(); j++) {
                //gray = mRgba.get(contours.get(i).get(j), contours.get(i).get(j));
                //Imgproc.circle(points, new Point(contours.get(i).get(j), contours.get(i).get(j)),1, new Scalar(gray, gray, gray));
            }
        }*/
        return borders;
    }


}
