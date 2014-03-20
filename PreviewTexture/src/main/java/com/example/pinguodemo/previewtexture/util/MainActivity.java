package com.example.pinguodemo.previewtexture.util;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.widget.FrameLayout;

import com.example.pinguodemo.previewtexture.GLProducerThread;
import com.example.pinguodemo.previewtexture.GLRendererImpl;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends Activity implements TextureView.SurfaceTextureListener, Camera.PreviewCallback {

    private Camera mCamera;
    private TextureView mTextureView;

    private GLRendererImpl mRenderer;
    private Thread mProducerThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);


        setContentView(mTextureView);
        mRenderer = new GLRendererImpl(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = Camera.open();

        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        mTextureView.setLayoutParams(new FrameLayout.LayoutParams(
                previewSize.width, previewSize.height, Gravity.CENTER));

        try {
            mCamera.setPreviewTexture(surface);
        } catch (IOException t) {
        }

        mCamera.setPreviewCallback(this);
        mCamera.startPreview();

        mRenderer.setViewport(width, height);
//        mProducerThread = new GLProducerThread(surface, mRenderer, new AtomicBoolean(true));
//        mProducerThread.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, the Camera does all the work for us
        mRenderer.resize(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Update your view here!
        Log.i("aaaa", " onSurfaceTextureUpdated = " + surface);

    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
//        Log.i("aaaa", " onPreviewFrame = " + bytes);
    }
}