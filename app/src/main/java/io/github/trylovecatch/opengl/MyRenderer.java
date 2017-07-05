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
 * 中间有一个红线的黄色长方形
 */

public class MyRenderer implements GLSurfaceView.Renderer{

    private Context context;

    private static final int POSITION_COMOPNENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
//    float[] tableVerticesWithTriangles = {
//            // Triangle 1
//            0f,  0f,
//            9f, 14f,
//            0f, 14f,
//
//            // Triangle 2
//            0f,  0f,
//            9f,  0f,
//            9f, 14f
//    };
    float[] tableVerticesWithTriangles = {
        // Triangle 1
        -0.5f, -0.5f,
        0.5f,  0.5f,
        -0.5f,  0.5f,

        // Triangle 2
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f,  0.5f,

        // Line 1
        -0.5f, 0f,
        0.5f, 0f,
    };
    private FloatBuffer buffer;

    private int program;

    private static final String U_COLOR = "u_Color";//和.glsl文件里面定义的一样
    private int attribColor;
    private static final String A_POSITION = "a_Position";//和.glsl文件里面定义的一样
    private int attribPosition;

    public MyRenderer(Context context){
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
                TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);//读取顶点着色器
        String fragmentShaderSource =
                TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);//读取片段着色器

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);//生成并编译顶点着色器
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);//生成并编译片段着色器

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);//将着色器附加到程序对象上并执行链接操作

        ShaderHelper.validateProgram(program); //验证

        GLES20.glUseProgram(program);//使用这个程序


        /**
            u_Color a_Position 都是一个 Uniform   Uniform 以及 其他两种变量详解： http://blog.csdn.net/renai2008/article/details/7844495
            Uniform是变量类型的一种修饰符,是OpenGL ES  中被着色器中的常量值,使用存储各种着色器需要的数据，例如：转换矩阵、光照参数或者颜色。
        　　uniform 的空间被顶点着色器和片段着色器分享。也就是说顶点着色器和片段着色器被链接到一起进入项目，它们分享同样的uniform。因此一个在顶点着色器中声明的uniform，相当于在片段着色器中也声明过了。
           当应用程序装载uniform 时，它的值在顶点着色器和片段着色器都可用。在链接阶段，链接器将分配常量在项目里的实际地址，那个地址是被应用程序使用和加载的标识。

        　　另一个需要注意的是，uniform 被存储在硬件被称为常量存储，这是一种分配在硬件上的存储常量值的空间。因为这种存储需要的空间是固定的，在程序中这种uniform 的数量是受限的。
            这个限制能通过读gl_MaxVertexUniformVectors 和gl_MaxFragmentUniformVectors编译变量得出。
           （ 或者用GL_MAX_VERTEX_UNIFORM_VECTORS 或GL_MAX_FRAGMENT_UNIFORM_ VECTORS 为参数调用glGetIntegerv）OpenGL ES 2.0必须至少提供256 个顶点着色器uniform 和224个片段着色器uniform。
        */
        attribColor = GLES20.glGetUniformLocation(program, U_COLOR);//获取 u_Color 在shader 中的指针
        attribPosition = GLES20.glGetAttribLocation(program, A_POSITION);//获取 a_Position 在 shader 中的指针

        buffer.position(0);//将读取指针复位
        GLES20.glVertexAttribPointer(attribPosition, POSITION_COMOPNENT_COUNT, GLES20.GL_FLOAT, false,
                0, buffer);// 指定了渲染时索引值为 attribPosition 的顶点属性数组的数据格式和位置
        GLES20.glEnableVertexAttribArray(attribPosition);// Enable or disable a generic vertex attribute array
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
     /*
    函数原型:
          void glClear(GLbitfield mask);
    参数说明：
          GLbitfield：可以使用 | 运算符组合不同的缓冲标志位，表明需要清除的缓冲，例如glClear（GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT）表示要清除颜色缓冲以及深度缓冲，可以使用以下标志位
          GL_COLOR_BUFFER_BIT:    当前可写的颜色缓冲
          GL_DEPTH_BUFFER_BIT:    深度缓冲
          GL_ACCUM_BUFFER_BIT:   累积缓冲
    　　  GL_STENCIL_BUFFER_BIT: 模板缓冲
    函数说明：
          glClear（）函数的作用是用当前缓冲区清除值，也就是glClearColor或者glClearDepth、glClearIndex、glClearStencil、glClearAccum等函数所指定的值来清除指定的缓冲区，也可以使用glDrawBuffer一次清除多个颜色缓存。比如：
    　　  glClearColor（0.0，0.0，0.0，0.0）;
    　　  glClear（GL_COLOR_BUFFER_BIT）;
    　　  第一条语句表示清除颜色设为黑色，第二条语句表示实际完成了把整个窗口清除为黑色的任务，glClear（）的唯一参数表示需要被清除的缓冲区。
         像素检验、裁剪检验、抖动和缓存的写屏蔽都会影响glClear的操作，其中，裁剪范围限制了清除的区域，而glClear命令还会忽略alpha函数、融合函数、逻辑操作、模板、纹理映射和z缓存；
     */
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);


        // Draw the table.
        // 指定uniform变量attribPosition的值
        // 后面四个是rgba色值
        GLES20.glUniform4f(attribColor, 1.0f, 1.0f, 0.0f, 1.0f);
        // 绘制桌子
        // 第一个参数 表示绘制的是三角形
        // 第二个参数 表示从顶点数据的开头开始读取顶点
        // 第三个参数 表示要读入6个顶点
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        GLES20.glUniform4f(attribColor, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);
    }
}
