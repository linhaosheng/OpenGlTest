package com.linhao.opengltest.one;

import android.content.Context;
import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.linhao.opengltest.R;

import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {

    private Triangle triangle;
    private Square square;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);

        OneGlSurfaceView oneGlSurfaceView = new OneGlSurfaceView(this);
        setContentView(oneGlSurfaceView);
    }

    public class OneGlSurfaceView extends GLSurfaceView {
        private final OneGlRenderer mRenderer;

        public OneGlSurfaceView(Context context) {
            super(context);
            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            mRenderer = new OneGlRenderer();
            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(mRenderer);

        }
    }

    public class OneGlRenderer implements GLSurfaceView.Renderer {

//        private Triangle triangle;
//
//        public OneGlRenderer(Triangle triangle){
//            this.triangle = triangle;
//        }

        public void onSurfaceCreated(GL10 unused, EGLConfig config) {
            // Set the background frame color
            //  GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        }

        private float[] mRotationMatrix = new float[16];

        public void onDrawFrame(GL10 unused) {
            float[] scratch = new float[16];

            // Redraw background color
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

            // 创建一个旋转矩阵
            long time = SystemClock.uptimeMillis() % 4000L;
            float angle = 0.090f * ((int) time);
            Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);

            // Calculate the projection and view transformation
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
            Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

            if (triangle != null) {
                triangle.draw(scratch);
            }
        }

        @Override
        public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            triangle = new Triangle(this);
            square = new Square();
        }

        private final float[] mMVPMatrix = new float[16];
        private final float[] mProjectionMatrix = new float[16];
        private final float[] mViewMatrix = new float[16];

        public void onSurfaceChanged(GL10 unused, int width, int height) {
            GLES20.glViewport(0, 0, width, height);

            float ratio = (float) width / height;

            // 这个投影矩阵被应用于对象坐标在onDrawFrame（）方法中
            Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        }


        public int loadShader(int type, String shaderCode) {

            // 创造顶点着色器类型(GLES20.GL_VERTEX_SHADER)
            // 或者是片段着色器类型 (GLES20.GL_FRAGMENT_SHADER)
            int shader = GLES20.glCreateShader(type);
            // 添加上面编写的着色器代码并编译它
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);
            return shader;
        }
    }
}
