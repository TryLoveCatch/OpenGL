package io.github.trylovecatch.opengl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class OpenGLSampleActivity extends AppCompatActivity {

    private int mType;

    private GLSurfaceView mGlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_gl_smaple);

        if(!isSupportGl()){
            finish();
            return;
        }

        mType = getIntent().getIntExtra("type", 0);

        mGlView = (GLSurfaceView)findViewById(R.id.gl_view);
        initGlView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGlView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGlView.onPause();
    }

    private void initGlView(){
        mGlView.setEGLContextClientVersion(2);
        switch (mType){
            case 0:
                mGlView.setRenderer(new MyRenderer(this));
                break;
            case 1:
                mGlView.setRenderer(new MyRenderer1(this));
                break;
            case 2:
                mGlView.setRenderer(new MyRenderer2(this));
                break;
            case 3:
                mGlView.setRenderer(new MyRenderer3(this));
                break;
            default:
                mGlView.setRenderer(new MyRenderer(this));
                break;
        }
//
        mGlView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private boolean isSupportGl(){
        final ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        if (supportsEs2) {
            return true;
        } else {
            Toast.makeText(this, "Not support OpenGL2.0", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}
