package org.proven.decisions2.PublicDecision;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.spec.ECField;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.proven.decisions2.R;


public class CameraActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);
    }

    @Override
    public void onConfigurationChanged(Configuration c){
        super.onConfigurationChanged(c);
        rotateCamera();
    }

    private static final String[] PERMISSIONS={
            Manifest.permission.CAMERA,
    };

    private static final int REQUEST_PERMISSIONS = 34;

    private static final int PERMISSIONS_COUNT = 1;

    private boolean arePermissionsDenied(){
        for (int i = 0; i< PERMISSIONS_COUNT ; i++){
            if (checkSelfPermission(PERMISSIONS[i])!= PackageManager.PERMISSION_GRANTED){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==REQUEST_PERMISSIONS && grantResults.length>0){
            if (arePermissionsDenied()){
                ((ActivityManager) (this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
                recreate();
            }else{
                onResume();
            }
        }
    }

    private boolean isCameraInitialized;

    private Camera mCamera = null;

    private static int mCameraId;

    private static SurfaceHolder myHolder;

    private static CameraPreview mPreview;

    private FrameLayout preview;

    private Button flashB;

    private static boolean fM;

    private static Camera.Parameters p;

    String flashMode;

    private void initCam() {
        int cameraFacing = whichCamera ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
        mCamera = Camera.open(cameraFacing);
        p = mCamera.getParameters();
        mPreview = new CameraPreview(this, mCamera);
        preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        rotateCamera();

        flashB = findViewById(R.id.flash);
        flash();
    }



    private void init(){
        initCam();
        final Button switchCameraButton = findViewById(R.id.rotate_camera);
        switchCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.release();
                switchCamera();
                rotateCamera();
                try {
                    mCamera.setPreviewDisplay(myHolder);
                }catch (Exception e){

                }
                mCamera.startPreview();
                if (mCameraId== Camera.CameraInfo.CAMERA_FACING_FRONT){
                    flashB.setBackgroundDrawable(ContextCompat.getDrawable(CameraActivity.this, R.drawable.ic_no_flash));
                    flashB.setEnabled(false);
                }else{
                    flashB.setBackgroundDrawable(ContextCompat.getDrawable(CameraActivity.this, R.drawable.ic_flash));
                    flashB.setEnabled(true);
                    flash();
                }

                // Set updated parameters to the camera
                mCamera.setParameters(p);
            }
        });

        final Button takePhotoButton = findViewById(R.id.takePhoto);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mCamera.takePicture(null, null, mPicture);
                }catch (Exception e){
                    System.out.println("Error taken picture");
                }
            }
        });

        preview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (whichCamera){
                    if (fM){
                        p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    }else{
                        p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    }
                    try{
                        mCamera.setParameters(p);
                    }catch (Exception e){

                    }
                    fM = !fM;
                }
                return true;
            }
        });

        flash();

        // Set updated parameters to the camera
        mCamera.setParameters(p);
    }

    private void flash(){
        if (hasFlash()) {
            p.setFlashMode(p.FLASH_MODE_OFF);
            flashB.setBackgroundDrawable(ContextCompat.getDrawable(CameraActivity.this, R.drawable.ic_no_flash));
            try {
                mCamera.setParameters(p);
            } catch (Exception e) {
                Log.e("TAG", "Error setting camera parameters: " + e.getMessage());
            }
            // Configura el botón de flash para encender y apagar el flash
            flashB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtiene los parámetros actualizados de la cámara
                    p = mCamera.getParameters();
                    flashMode = p.getFlashMode();

                    // Si el flash está apagado, lo enciende
                    if (p.FLASH_MODE_OFF.equals(flashMode)) {
                        p.setFlashMode(p.FLASH_MODE_ON);
                        try {
                            mCamera.setParameters(p);
                        } catch (Exception e) {
                            Log.e("TAG", "Error setting camera parameters: " + e.getMessage());
                        }
                        flashB.setBackgroundDrawable(ContextCompat.getDrawable(CameraActivity.this, R.drawable.ic_flash));
                        System.out.println("FLASH ON");
                    }
                    // Si el flash está encendido, lo apaga
                    else if (p.FLASH_MODE_ON.equals(flashMode)) {
                        p.setFlashMode(p.FLASH_MODE_OFF);
                        try {
                            mCamera.setParameters(p);
                        } catch (Exception e) {
                            Log.e("TAG", "Error setting camera parameters: " + e.getMessage());
                        }
                        flashB.setBackgroundDrawable(ContextCompat.getDrawable(CameraActivity.this, R.drawable.ic_no_flash));
                        System.out.println("FLASH OFF");
                    }else{
                        System.out.println("Problems with flash : init");
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N && arePermissionsDenied()){
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            return;
        }
        if (!isCameraInitialized){
            init();
            isCameraInitialized=true;
        }else{
            initCam();
        }
    }


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // Decodifica el byte array de la imagen en un objeto Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            // Rota la imagen si es necesario
            bitmap = rotateBitmap(bitmap, getCameraDisplayOrientation(CameraActivity.this, camera));

            // Convierte el bitmap en un array de bytes
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
            byte[] byteArray = stream.toByteArray();

            // Crea un Intent para pasar los bytes de la imagen a la siguiente actividad
            Intent intent = getIntent();
            String textoDecision1 = intent.getStringExtra("decision1");
            String textoDecision2 = intent.getStringExtra("decision2");
            System.out.println("CameraActivity "+textoDecision1);
            System.out.println("CameraActivity "+textoDecision2);
            Intent intent4 = new Intent(CameraActivity.this, ResultPhoto.class);
            intent4.putExtra("photo", byteArray);
            intent4.putExtra("decision1", textoDecision1);
            intent4.putExtra("decision2", textoDecision2);
            // Inicie la siguiente actividad
            startActivity(intent4);

        }
    };


    private static Bitmap rotateBitmap(Bitmap bitmap, int rotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            matrix.postScale(-1, 1, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }




    private static int getCameraDisplayOrientation(Activity activity, Camera camera) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info); // pass the current camera ID here
        int result;
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation - degrees + 360) % 360;
            System.out.println("A");
        } else { // back-facing
            result = (info.orientation + degrees) % 360;
            System.out.println("B");
        }
        return result;
    }

    private void switchCamera() {

        mCamera.release(); // Libera la cámara actual
        if (whichCamera) {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            mCamera = Camera.open(); // Abre la cámara trasera
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }

        if (hasFlash()) {
            p = mCamera.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            flashB.setBackgroundDrawable(ContextCompat.getDrawable(CameraActivity.this, R.drawable.ic_no_flash));
            mCamera.setParameters(p);
        }

        whichCamera = !whichCamera;

        // Obtén los parámetros actualizados de la nueva cámara
        p = mCamera.getParameters();

        // Establece los parámetros actualizados en la nueva cámara
        mCamera.setParameters(p);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }


    private void releaseCamera(){
        if (mCamera != null){
            preview.removeView(mPreview);
            mCamera.release();
            mCamera = null;
        }
    }


    private boolean hasFlash() {
        if (mCamera == null) {
            return false;
        }

        Camera.Parameters parameters = mCamera.getParameters();
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes == null) {
            flashB.setVisibility(View.INVISIBLE);
            return false;
        }

        for (String flashMode : flashModes) {
            if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
                flashB.setVisibility(View.VISIBLE);
                return true;
            }
        }
        return false;
    }


    private static int rotation;

    private static boolean whichCamera=true;



    private void rotateCamera(){
        if (mCamera!=null){
            rotation = this.getWindowManager().getDefaultDisplay().getRotation();
            if (rotation == 0){
                rotation = 90;
            }else if (rotation == 1){
                rotation = 0;
            }else if (rotation == 2){
                rotation = 270;
            }else{
                rotation = 180;
            }
            mCamera.setDisplayOrientation(rotation);
            if (!whichCamera){
                if (rotation==90){
                    rotation=270;
                }else if (rotation==270){
                    rotation = 90;
                }
            }
            p = mCamera.getParameters();
            p.setRotation(rotation);
            mCamera.setParameters(p);
        }
    }

    private static class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
        private static SurfaceHolder mHolder;
        private static Camera mCamera;

        private CameraPreview(Context context, Camera camera){
            super(context);
            mCamera = camera;
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void  surfaceCreated(SurfaceHolder holder){
            myHolder = holder;
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();

            }catch (IOException e){
                e.printStackTrace();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder){

        }

        public void surfaceChanged(SurfaceHolder holder, int f, int w, int h){

        }
    }
}
