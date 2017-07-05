package io.github.trylovecatch.opengl;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

/**
 * Created by lipeng21 on 2017/6/21.
 *
 * 1、用三角形扇绘制一个长方形
 * 2、长方形，中间明亮，四周暗淡
 */

public class MyRenderer1 implements GLSurfaceView.Renderer{


    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMOPNENT_COUNT = 2;
    private static final int COLOR_COMOPNENT_COUNT = 3;
    private static final String A_COLOR = "a_Color";
    private static final int STRIDE = (POSITION_COMOPNENT_COUNT + COLOR_COMOPNENT_COUNT) * BYTES_PER_FLOAT;


    private Context context;
    float[] tableVerticesWithTriangles = {
            0f,    0f,   1f,   1f,   1f,  //一个顶点 有两个属性：position和color，position有两个属性值构成，color有三个属性值构成
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
            0.5f,  0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f,  0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

            // Line 1
            -0.5f, 0f, 1f, 0f, 0f,
            0.5f, 0f, 1f, 0f, 0f,
    };
    private FloatBuffer buffer;

    private int program;

    private int attribColor;
    private static final String A_POSITION = "a_Position";//和.glsl文件里面定义的一样
    private int attribPosition;

    public MyRenderer1(Context context){
        this.context = context;

        buffer = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        buffer.put(tableVerticesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexShaderSource =
                TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader1);//读取顶点着色器
        String fragmentShaderSource =
                TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader1);//读取片段着色器

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);//生成并编译顶点着色器
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);//生成并编译片段着色器

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);//将着色器附加到程序对象上并执行链接操作

        ShaderHelper.validateProgram(program); //验证

        GLES20.glUseProgram(program);//使用这个程序


        attribColor = GLES20.glGetAttribLocation(program, A_COLOR);//获取 a_Color 在shader 中的指针
        attribPosition = GLES20.glGetAttribLocation(program, A_POSITION);//获取 a_Position 在 shader 中的指针

        /**
         * 取位置
         */
        buffer.position(0);//将读取指针复位
        GLES20.glVertexAttribPointer(attribPosition, POSITION_COMOPNENT_COUNT, GLES20.GL_FLOAT, false,
                STRIDE, buffer);// 指定了渲染时索引值为 attribPosition 的顶点属性数组的数据格式和位置
        GLES20.glEnableVertexAttribArray(attribPosition);// Enable or disable a generic vertex attribute array

        /**
         * 取颜色
         */
        buffer.position(POSITION_COMOPNENT_COUNT);
        GLES20.glVertexAttribPointer(attribColor, COLOR_COMOPNENT_COUNT, GLES20.GL_FLOAT, false,
                STRIDE, buffer);
        GLES20.glEnableVertexAttribArray(attribColor);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);


        // 绘制桌子
        // 第一个参数 表示绘制的是三角形扇
        // 第二个参数 表示从顶点数据的开头开始读取顶点
        // 第三个参数 表示要读入6个顶点
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);
    }
}
