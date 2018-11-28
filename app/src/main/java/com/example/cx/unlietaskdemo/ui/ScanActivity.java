package com.example.cx.unlietaskdemo.ui;

import android.Manifest;
import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.arseeds.ar.CameraPreview;
import com.arseeds.ar.ScanCallback;
import com.arseeds.ar.view.ScanView;
import com.xstore.tms.android.R;
import com.xstore.tms.android.base.BaseActivityKt;
import com.xstore.tms.android.ui.MainActivityKt;

import java.math.BigDecimal;


public class ScanActivity extends BaseActivityKt {

    private CameraPreview cameraPreview;
    private ImageView iv_scan_line;
    private SoundPool soundPool;
    private ScanView scanView;
    private ImageView mo_scanner_back;
    private Camera camera;

    @Override
    protected void onStart() {
        super.onStart();
        checkPermissions(new String[]{Manifest.permission.CAMERA});
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraPreview != null) {
            cameraPreview.setScanCallback(resultCallback);
            cameraPreview.start();
        }
        scanView.onResume();
    }

    @Override
    protected void initData() {
        super.initData();
        cameraPreview = (CameraPreview) findViewById(com.arseeds.ar.R.id.cp);
        iv_scan_line = (ImageView) findViewById(com.arseeds.ar.R.id.iv_scan_line);
        iv_scan_line = (ImageView) findViewById(com.arseeds.ar.R.id.iv_scan_line);

        //bi~
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(this, com.arseeds.ar.R.raw.qrcode, 1);

        scanView = (ScanView) findViewById(com.arseeds.ar.R.id.sv);
        scanView.startScan();

        mo_scanner_back = (ImageView) findViewById(com.arseeds.ar.R.id.mo_scanner_back);
        mo_scanner_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ScanActivity.this.finish();
            }
        });
        TextView open = (TextView) findViewById(R.id.open_flashlight);
        open.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                turnOnFlash();
            }
        });
        TextView close = (TextView) findViewById(R.id.close_flashlight);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                turnOffFlash();
            }
        });
    }

    public void turnOnFlash() {
        if (null != cameraPreview) {
            Camera camera = cameraPreview.getmCameraManager().getCamera();
            if (camera != null) {
                try {
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void turnOffFlash() {
        if (null != cameraPreview) {
            Camera camera = cameraPreview.getmCameraManager().getCamera();
            if (camera != null) {
                try {
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameters);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ScanCallback resultCallback = new ScanCallback() {

        @Override
        public void onScanResult(String result) {

            soundPool.play(1, 1, 1, 0, 0, 1);

            Intent resultIntent = getIntent();
            Bundle bundle = new Bundle();
            bundle.putString("result", result);
            resultIntent.putExtras(bundle);
            setResult(MainActivityKt.RESULT_CODE_SCAN, resultIntent);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraPreview.stop();
        soundPool.release();
        if (camera != null) {
            camera.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraPreview != null) {
            cameraPreview.stop();
        }

        scanView.onPause();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_scan;
    }
}
