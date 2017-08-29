package io.github.trylovecatch.opengl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private CameraToMpeg mCameraToMpeg;
    private boolean mIsRunning;


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
                if(mIsRunning){
                    mIsRunning = false;
                    mCameraToMpeg.mIsRunning = false;

                    ((Button)findViewById(R.id.camera_filter_2_mp4)).setText("摄像头保存成MP4并滤镜");
                }else {
                    mIsRunning = true;
                    new Thread() {
                        @Override
                        public void run() {
                            mCameraToMpeg = new CameraToMpeg();
                            try {
                                CameraToMpeg.CameraToMpegWrapper.runTest(mCameraToMpeg);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    }.start();

                    ((Button)findViewById(R.id.camera_filter_2_mp4)).setText("停止");
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
