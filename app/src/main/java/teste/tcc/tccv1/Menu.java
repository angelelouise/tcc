package teste.tcc.tccv1;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;

//import teste.tcc.tccv1.Main_Show_Camera;


public class Menu extends AppCompatActivity {
    ImageButton cam1;
    ImageButton cam2;
    boolean tag_f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        cam1 = (ImageButton) findViewById(R.id.cam1);
        cam1.setOnClickListener(cam1handler);
        cam2 = (ImageButton) findViewById(R.id.cam2);
        //cam2.setOnClickListener(cam2handler);
    }

    View.OnClickListener cam1handler = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            startSecondActivity(v);
            tag_f=false;
        }

    };
    /*View.OnClickListener cam2handler = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            startSecondActivity(v);
            tag_f=true;
        }

    };*/
    public void startSecondActivity(View view) {//Alternado entre duas activies
        Intent secondActivity = new Intent(this, Main_Show_Camera.class);
        startActivity(secondActivity);
    }

}
