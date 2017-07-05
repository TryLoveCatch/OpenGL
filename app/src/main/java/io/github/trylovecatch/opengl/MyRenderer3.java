package io.github.trylovecatch.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

/**
 * Created by lipeng21 on 2017/6/21.
 *
 * 1、用三角形扇绘制一个长方形
 * 2、长方形，中间明亮，四周暗淡
 */

public class MyRenderer3 implements GLSurfaceView.Renderer{
    private static final String TAG = MyRenderer3.class.getSimpleName();


    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMOPNENT_COUNT = 2;
    private static final int COLOR_COMOPNENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMOPNENT_COUNT + COLOR_COMOPNENT_COUNT) * BYTES_PER_FLOAT;


    private Context context;
    private final float[] sPos={
            -1.0f,1.0f,
            -1.0f,-1.0f,
            1.0f,1.0f,
            1.0f,-1.0f
    };

    private final float[] sCoord={
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,0.0f,
            1.0f,1.0f,
//            0.5f,0.5f,
//            0.5f,0.5f,
//            0.5f,0.5f,
//            0.5f,0.5f,
    };
    private FloatBuffer bufferPos;
    private FloatBuffer bufferCoord;

    private int program;

    private final float[] matrix = new float[16];
    private static final String U_MATRIX = "u_Matrix";
    private int uMatrix;


    private static final String A_COORDINATE = "a_Coordinate";//和.glsl文件里面定义的一样
    private int attribCoord;
    private static final String A_POSITION = "a_Position";//和.glsl文件里面定义的一样
    private int attribPosition;

    private static final String U_TEXTURE = "u_Texture";//和.glsl文件里面定义的一样
    private int uTexture;
    private int mTexture;
    private Bitmap mBitmap;



    private final float[] sPosIcon={
            -0.5f,0.5f,
            -0.5f,-0.5f,
            0.5f,0.5f,
            0.5f,-0.5f
    };

    private final float[] sCoordIcon={
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,0.0f,
            1.0f,1.0f,
    };
    private FloatBuffer bufferPosIcon;
    private FloatBuffer bufferCoordIcon;
    private int programIcon;
    private static final String A_COORDINATE_ICON = "a_Coordinate";//和.glsl文件里面定义的一样
    private int attribCoordIcon;
    private static final String A_POSITION_ICON = "a_Position";//和.glsl文件里面定义的一样
    private int attribPositionIcon;

    private static final String U_TEXTURE_ICON = "u_Texture";//和.glsl文件里面定义的一样
    private int uTextureIcon;
    private int mTextureIcon;
    private Bitmap mBitmapIcon;

    public MyRenderer3(Context context){
        this.context = context;

        bufferPos = ByteBuffer
                .allocateDirect(sPos.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        bufferPos.put(sPos);

        bufferCoord = ByteBuffer
                .allocateDirect(sCoord.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        bufferCoord.put(sCoord);


        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fengj);


        bufferPosIcon = ByteBuffer
                .allocateDirect(sPosIcon.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        bufferPosIcon.put(sPosIcon);

        bufferCoordIcon = ByteBuffer
                .allocateDirect(sCoordIcon.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        bufferCoordIcon.put(sCoordIcon);


        mBitmapIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.fex);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
//        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        String vertexShaderSource =
                TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader2);//读取顶点着色器
        String fragmentShaderSource =
                TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader2);//读取片段着色器

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);//生成并编译顶点着色器
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);//生成并编译片段着色器

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);//将着色器附加到程序对象上并执行链接操作


        attribCoord = GLES20.glGetAttribLocation(program, A_COORDINATE);//获取 a_Color 在shader 中的指针
        ShaderHelper.checkGlError(TAG, "glGetAttribLocation attribCoord");
        attribPosition = GLES20.glGetAttribLocation(program, A_POSITION);//获取 a_Position 在 shader 中的指针
        ShaderHelper.checkGlError(TAG, "glGetAttribLocation attribPosition");
        uTexture = GLES20.glGetUniformLocation(program, U_TEXTURE);//获取 a_Position 在 shader 中的指针
        ShaderHelper.checkGlError(TAG, "glGetUniformLocation uTexture");
        uMatrix = GLES20.glGetUniformLocation(program, U_MATRIX);
        ShaderHelper.checkGlError(TAG, "glGetUniformLocation uMatrix");







        vertexShaderSource =
                TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader3);//读取顶点着色器
        fragmentShaderSource =
                TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader3);//读取片段着色器

        vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);//生成并编译顶点着色器
        fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);//生成并编译片段着色器

        programIcon = ShaderHelper.linkProgram(vertexShader, fragmentShader);//将着色器附加到程序对象上并执行链接操作

        attribCoordIcon = GLES20.glGetAttribLocation(programIcon, A_COORDINATE_ICON);//获取 a_Color 在shader 中的指针
        ShaderHelper.checkGlError(TAG, "glGetAttribLocation attribCoord");
        attribPositionIcon = GLES20.glGetAttribLocation(programIcon, A_POSITION_ICON);//获取 a_Position 在 shader 中的指针
        ShaderHelper.checkGlError(TAG, "glGetAttribLocation attribPosition");
        uTextureIcon = GLES20.glGetUniformLocation(programIcon, U_TEXTURE_ICON);//获取 a_Position 在 shader 中的指针
        ShaderHelper.checkGlError(TAG, "glGetUniformLocation uTexture");


        int[] textureIds = createTexture();
        mTexture = textureIds[0];
        mTextureIcon = textureIds[1];


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);

        final float tRatio = width > height ? 1.0f * width / height : 1.0f * height / width;
        if(width > height){
            Matrix.orthoM(matrix, 0, -tRatio, tRatio, -1f, 1f, -1f, 1f);
        }else{
            Matrix.orthoM(matrix, 0, -1f, 1f, -tRatio, tRatio,  -1f, 1f);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        ShaderHelper.checkGlError(TAG, "glClear");
        GLES20.glUseProgram(program);//使用这个程序
        ShaderHelper.checkGlError(TAG, "glUseProgram");

        GLES20.glUniformMatrix4fv(uMatrix, 1, false, matrix, 0);

        bufferPos.position(0);
        GLES20.glVertexAttribPointer(attribPosition, 2, GLES20.GL_FLOAT, false, 0, bufferPos);
        ShaderHelper.checkGlError(TAG, "glVertexAttribPointer attribPosition");
        GLES20.glEnableVertexAttribArray(attribPosition);
        ShaderHelper.checkGlError(TAG, "glEnableVertexAttribArray attribPosition");

        bufferCoord.position(0);
        GLES20.glVertexAttribPointer(attribCoord, 2, GLES20.GL_FLOAT, false, 0, bufferCoord);
        ShaderHelper.checkGlError(TAG, "glVertexAttribPointer attribCoord");
        GLES20.glEnableVertexAttribArray(attribCoord);
        ShaderHelper.checkGlError(TAG, "glEnableVertexAttribArray attribCoord");


        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        ShaderHelper.checkGlError(TAG, "glActiveTexture attribCoord");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture);
        ShaderHelper.checkGlError(TAG, "glBindTexture attribCoord");
        GLES20.glUniform1i(uTexture, 0);
        ShaderHelper.checkGlError(TAG, "glUniform1i attribCoord");

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        ShaderHelper.checkGlError(TAG, "glDrawArrays attribCoord");



        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc (GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        // 先画的目标对象，后画的是源对象
        // 这里 上面的texture0是目标对象
        // texture1 是源对象
//        GLES20.glBlendFunc (GLES20.GL_ZERO, GLES20.GL_ONE);
        GLES20.glUseProgram(programIcon);//使用这个程序
        ShaderHelper.checkGlError(TAG, "glUseProgram");

        bufferPosIcon.position(0);
        GLES20.glVertexAttribPointer(attribPositionIcon, 2, GLES20.GL_FLOAT, false, 0, bufferPosIcon);
        ShaderHelper.checkGlError(TAG, "glVertexAttribPointer attribPosition");
        GLES20.glEnableVertexAttribArray(attribPositionIcon);
        ShaderHelper.checkGlError(TAG, "glEnableVertexAttribArray attribPosition");

        bufferCoordIcon.position(0);
        GLES20.glVertexAttribPointer(attribCoordIcon, 2, GLES20.GL_FLOAT, false, 0, bufferCoordIcon);
        ShaderHelper.checkGlError(TAG, "glVertexAttribPointer attribCoord");
        GLES20.glEnableVertexAttribArray(attribCoordIcon);
        ShaderHelper.checkGlError(TAG, "glEnableVertexAttribArray attribCoord");


        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        ShaderHelper.checkGlError(TAG, "glActiveTexture attribCoord");
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureIcon);
//        ShaderHelper.checkGlError(TAG, "glBindTexture attribCoord");
//        GLES20.glUniform1i(uTextureIcon, 1);
//        ShaderHelper.checkGlError(TAG, "glUniform1i attribCoord");
        /**
         * 这样把glUniform1i 1 改为0
         * 就意味着这个uTextureIcon 和纹理单元为0的纹理绑定了
         * 所以也会显示fengj这张图了
         */
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture);
//        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmapIcon, 0);
        ShaderHelper.checkGlError(TAG, "glBindTexture attribCoord");
        GLES20.glUniform1i(uTextureIcon, 0);
        ShaderHelper.checkGlError(TAG, "glUniform1i attribCoord");

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        ShaderHelper.checkGlError(TAG, "glDrawArrays attribCoord");
        GLES20.glDisable(GLES20.GL_BLEND);
    }


    private int[] createTexture(){
        int[] texture=new int[2];
        //生成纹理
        GLES20.glGenTextures(2,texture,0);
        ShaderHelper.checkGlError(TAG, "glGenTextures attribCoord");
        //生成纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
        ShaderHelper.checkGlError(TAG, "glBindTexture attribCoord");
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        //根据以上指定的参数，生成一个2D纹理
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
        ShaderHelper.checkGlError(TAG, "texImage2D attribCoord");

        //生成纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[1]);
        ShaderHelper.checkGlError(TAG, "glBindTexture attribCoord");
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);
        //根据以上指定的参数，生成一个2D纹理
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmapIcon, 0);
//        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        ShaderHelper.checkGlError(TAG, "texImage2D attribCoord");
        return texture;
    }

}
