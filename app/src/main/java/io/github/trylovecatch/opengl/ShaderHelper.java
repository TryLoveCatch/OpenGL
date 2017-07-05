package io.github.trylovecatch.opengl;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by lipeng21 on 2017/6/21.
 */

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    /**
     * Loads and compiles a vertex shader, returning the OpenGL object ID.
     *
     * 顶点着色器
     */
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    /**
     * Loads and compiles a fragment shader, returning the OpenGL object ID.
     *
     * 片段着色器
     */
    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    /**
     * Compiles a shader, returning the OpenGL object ID.
     */
    private static int compileShader(int type, String shaderCode) {

        // Create a new shader object.
        // 创建一个着色器，顶点或者片段，返回一个int 可以理解为这个对象的指针
        // 0表示失败
        final int shaderObjectId = glCreateShader(type);

        if (shaderObjectId == 0) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "Could not create new shader.");
            }

            return 0;
        }

        // Pass in the shader source.
        // 将我们刚刚读取的着色器代码关联到着色器对象
        glShaderSource(shaderObjectId, shaderCode);

        // Compile the shader.
        // 编译上面的着色器代码
        glCompileShader(shaderObjectId);

        // Get the compilation status.
        // 编译过后，我们需要取出编译状态以及在错误时取出信息日志
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

        if (BuildConfig.DEBUG) {
            // Print the shader info log to the Android log output.
            Log.v(TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:"
                    + glGetShaderInfoLog(shaderObjectId));
        }

        // Verify the compile status.
        // 为0 就是编译失败
        if (compileStatus[0] == 0) {
            // If it failed, delete the shader object.
            glDeleteShader(shaderObjectId);

            if (BuildConfig.DEBUG) {
                Log.w(TAG, "Compilation of shader failed.");
            }

            return 0;
        }

        // Return the shader object ID.
        // 返回着色器对象的指针
        return shaderObjectId;
    }
    /**
     * Links a vertex shader and a fragment shader together into an OpenGL
     * program. Returns the OpenGL program object ID, or 0 if linking failed.
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {

        // Create a new program object.
        // 创建一个程序对象 返回值 int依然是指针
        final int programObjectId = glCreateProgram();

        // 指针==0 就是创建失败
        if (programObjectId == 0) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "Could not create new program");
            }

            return 0;
        }

        // Attach the vertex shader to the program.
        // 顶点着色器附加到程序对象
        glAttachShader(programObjectId, vertexShaderId);
        // Attach the fragment shader to the program.
        // 片段着色器附加到程序对象
        glAttachShader(programObjectId, fragmentShaderId);

        // Link the two shaders together into a program.
        // 链接着色器和程序对象
        glLinkProgram(programObjectId);

        // Get the link status.
        // 判断是否链接成功了
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

        if (BuildConfig.DEBUG) {
            // Print the program info log to the Android log output.
            Log.v(TAG, "Results of linking program:\n"
                    + glGetProgramInfoLog(programObjectId));
        }

        // Verify the link status.
        if (linkStatus[0] == 0) {
            // If it failed, delete the program object.
            glDeleteProgram(programObjectId);
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "Linking of program failed.");
            }
            return 0;
        }

        // Return the program object ID.
        // 返回程序对象指针
        return programObjectId;
    }
    /**
     * Validates an OpenGL program. Should only be called when developing the
     * application.
     *
     * 验证程序对象是否可用
     *
     */
    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Results of validating program: " + validateStatus[0]
                + "\nLog:" + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }


    public static void checkGlError(String TAG, String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }
}
