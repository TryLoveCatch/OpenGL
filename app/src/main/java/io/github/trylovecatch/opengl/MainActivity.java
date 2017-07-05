package io.github.trylovecatch.opengl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.opengl_sample_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tIntent = new Intent(MainActivity.this, OpenGLSampleActivity.class);
                tIntent.putExtra("type", 0);
                startActivity(tIntent);
            }
        });
        findViewById(R.id.opengl_sample_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tIntent = new Intent(MainActivity.this, OpenGLSampleActivity.class);
                tIntent.putExtra("type", 1);
                startActivity(tIntent);
            }
        });
        findViewById(R.id.opengl_texture_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tIntent = new Intent(MainActivity.this, OpenGLSampleActivity.class);
                tIntent.putExtra("type", 2);
                startActivity(tIntent);
            }
        });
        findViewById(R.id.opengl_texture_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tIntent = new Intent(MainActivity.this, OpenGLSampleActivity.class);
                tIntent.putExtra("type", 3);
                startActivity(tIntent);
            }
        });

        findViewById(R.id.opengl_2_mp4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EncodeAndMux().testEncodeVideoToMp4();
            }
        });
        findViewById(R.id.camera_filter_2_mp4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CameraToMpeg.CameraToMpegWrapper.runTest(new CameraToMpeg());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
