package ng.com.easyworld.easyworld;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Oisrael on 3/12/2018.
 */

class MyRenderer implements GLSurfaceView.Renderer{

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        GLES20.glViewport(0,0,100,100);
        GLES20.glClearColor(0.0f,0.505f,0.50f,1.0f);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        GLES20.glViewport(0,0,100,100);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }
}
