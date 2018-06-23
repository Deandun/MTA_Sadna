package com.example.galbenabu1.classscanner;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.net.Uri;
import android.os.Build;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Handler;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.security.Policy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TakePicActivity extends AppCompatActivity {

    private static String TAG = "TakePicActivity";
    private static int DELAY_BETWEEN_PICTURES = 5;
    private static final int REQUEST_CAMERA_PERMISSION_RESULT = 0;
    private static final SparseIntArray CAMERA_ORIENTATIONS = new SparseIntArray();
    static {
        CAMERA_ORIENTATIONS.append(Surface.ROTATION_0, 0);
        CAMERA_ORIENTATIONS.append(Surface.ROTATION_90, 90);
        CAMERA_ORIENTATIONS.append(Surface.ROTATION_180, 180);
        CAMERA_ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    private CaptureRequest.Builder mCaptureRequestBuilder;
    private boolean mIsInProgress = false;

    //FireBase
    private StorageReference mStorageRef;

    //Take pictures in a loop
    private final Handler mHandler = new Handler();
    private int mPictureNumber = 0;

    private final Runnable mTakePictureRunnable = new Runnable() {
        public void run() {
           if(mIsInProgress) {
                mPictureNumber++;
                mIVSavedPicture.setImageBitmap(mTextureView.getBitmap());
                uploadImage(mPictureNumber);
                Toast.makeText(getApplicationContext(), "Taking a picture", Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(mTakePictureRunnable, 1000 * DELAY_BETWEEN_PICTURES);
           }
        }
    };


    //Hardware
    private String mCameraID;
    private CameraDevice mCameraDevice;
    private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
            Toast.makeText(getApplicationContext(), "Camera Disconnected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            mCameraDevice.close();
            mCameraDevice = null;
            Toast.makeText(getApplicationContext(), "Camera related error", Toast.LENGTH_SHORT).show();
        }
    };

    //UI
    private Size mPreviewSize;
    private Button mBtnTakePicture;
    private TextureView mTextureView;
    private ImageView mIVSavedPicture;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            setupCamera(width, height);
            connectCamera();
            Log.e(TAG, "Width: " + width + ". Height: " + height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    //Threads
    private HandlerThread mBackgroundHandlerThread;
    private Handler mBackgroundHandler;

    // App life cycle functions
    @Override
    protected void onResume() {
        super.onResume();
        this.startBackgroundThread();
        if (mTextureView.isAvailable()) {
            this.setupCamera(mTextureView.getWidth(), mTextureView.getHeight());
            connectCamera();
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION_RESULT) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "App cannot run without camera services", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pic);
        mTextureView = findViewById(R.id.bestTextureView);
        mBtnTakePicture = findViewById(R.id.btnTakeAPic);
        mIVSavedPicture = findViewById(R.id.ivSavedPic);
        mStorageRef = FirebaseStorage.getInstance().getReference("images");
    }

    @Override
    protected void onPause() {
        this.closeCamera();
        this.stopBackgroundThread();
        super.onPause();
    }

    // Camera
    private void setupCamera(int width, int height) {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraID : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraID);
                if (cameraCharacteristics.get(cameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                // Forces the camera orientation to be in landscape mode
                int deviceOrientation = getWindowManager().getDefaultDisplay().getRotation();
                int totalRotation = sensorToDeviceRotation(cameraCharacteristics, deviceOrientation);
                boolean swapRotation = totalRotation == 90 || totalRotation == 270; // Check if in portrait mode
                int rotatedWidth = swapRotation ? height : width;
                int rotatedHeight = swapRotation ? width : height;

                StreamConfigurationMap scaleStreamConfigsMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                mPreviewSize = chooseOptimalSize(scaleStreamConfigsMap.getOutputSizes(SurfaceTexture.class), rotatedWidth, rotatedHeight);
                mCameraID = cameraID;
                Log.e(TAG, "Camera ID: " + mCameraID + "With optimal size: " + mPreviewSize);
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void connectCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //Maybe remove this, depend on the minimum android version we decide on
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraManager.openCamera(mCameraID, mCameraDeviceStateCallback, mBackgroundHandler);
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        Toast.makeText(this, "App requires access to camera", Toast.LENGTH_SHORT).show();
                    }

                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION_RESULT);
                }
            } else {
                cameraManager.openCamera(mCameraID, mCameraDeviceStateCallback, mBackgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void startPreview() {
        SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        Surface previewSurface = new Surface(surfaceTexture);

        try {
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.addTarget(previewSurface);

            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    try {
                        cameraCaptureSession.setRepeatingRequest(mCaptureRequestBuilder.build(), null, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(getApplicationContext(), "Unable to setup camera preview", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    private static int sensorToDeviceRotation(CameraCharacteristics cameraCharacteristics, int deviceOrientation) {
        Log.e(TAG, "sensorToDeviceRotation >>");

        int sensorOrientation;

        try {
            sensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        } catch(Exception e) {
            Log.e(TAG, "sensorToDeviceRotation >> Exception thrown: " + e.getMessage());
            sensorOrientation = 0;
        }

        deviceOrientation = CAMERA_ORIENTATIONS.get(deviceOrientation);

        return (sensorOrientation + deviceOrientation + 360) % 360;
    }

    public void onTakeAPicClick(View v) {
        Log.e(TAG, "onTakeAPicClick >>");

        if(!mIsInProgress) {
            // Start repetative action to take a picture, every 15 seconds
            mHandler.post(mTakePictureRunnable);
            mBtnTakePicture.setText("Stop");
            mIsInProgress = true;
        } else {
            mBtnTakePicture.setText("Start");
            mIsInProgress = false;
        }
    }

    //Thread
    private void startBackgroundThread() {
        mBackgroundHandlerThread = new HandlerThread("Camera2APISample");
        mBackgroundHandlerThread.start();
        mBackgroundHandler = new Handler(mBackgroundHandlerThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundHandlerThread.quitSafely();
        try {
            mBackgroundHandlerThread.join();
            mBackgroundHandlerThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Preview Size
    private Size chooseOptimalSize(Size[] choices, int width, int height) {
        List<Size> bigEnoughSizes = new ArrayList<Size>();

        for (Size option : choices) {
            //if current size is big enough to display camera preview, add to list
            if (option.getHeight() == option.getWidth() * height / width && option.getWidth() >= width && option.getHeight() >= height) {
                bigEnoughSizes.add(option);
            }
        }

        if (bigEnoughSizes.size() > 1) {
            return Collections.min(bigEnoughSizes, new SizeComparator());
        } else {
            return choices[0];
        }
    }

    private static class SizeComparator implements Comparator<Size> {
        @Override
        public int compare(Size left, Size right) {
            return Long.signum((long) left.getWidth() * left.getHeight() / (long) right.getWidth() * right.getHeight());
        }
    }

    private void uploadImage(int imageNumber) {
        //Prepare image to be uploaded
        Log.e(TAG, "Preparing image for upload");
        mIVSavedPicture.setDrawingCacheEnabled(true);
        mIVSavedPicture.buildDrawingCache();
        Bitmap bitmap = mIVSavedPicture.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Upload image
        Log.e(TAG, "Uploading image...");
        StorageReference imageRef = mStorageRef.child(userId).child(Integer.toString(imageNumber));
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e(TAG, "Failed reading from storage.");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.e(TAG, "Successfully read " + downloadUrl.toString() + " from storage!");
                Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

