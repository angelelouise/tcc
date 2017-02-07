package teste.tcc.tccv1;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.CvType.CV_8U;

/**
 * Created by AngeleLouise on 05/10/2016.
 */

public class filtro1 {

    Mat mat_alterada;
    Mat mRgba;
    Mat mGray;
    Mat hierarquia;
    List<MatOfPoint> contornos;
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
        contornos = new ArrayList<MatOfPoint>();
        hierarquia = new Mat();
        borders = new Mat(height, width, CV_8U, new Scalar(255));
        Mat points = new Mat(height, width, CV_8U, new Scalar(255));


        Imgproc.putText(mRgba,"filtro 2", new Point(30,50),1, 2, new Scalar(255));
        Imgproc.Canny(mGray, borders, 80, 100);
        //Imgproc.cvtColor(borders, gray, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.findContours(borders, contornos, hierarquia, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        //desenhando os círculos
        for (int contourIdx = 0; contourIdx < contornos.size(); contourIdx++) {
            Imgproc.drawContours(mRgba, contornos, contourIdx, new Scalar(0, 0, 255), -1);
        }
        hierarquia.release();

        for(int i = 0; i< contornos.size(); i++) {
            for (int j = 0; j< contornos.get(i).toArray().length; j++) {
                Point[] aux= contornos.get(i).toArray();
                double[] gray = mRgba.get(i,j);
                System.out.println(aux);
                Imgproc.circle(points, new Point(aux[j].x, aux[j].y),1, new Scalar(gray[0], gray[1], gray[2]));
            }
        }
        return borders;
    }


}
