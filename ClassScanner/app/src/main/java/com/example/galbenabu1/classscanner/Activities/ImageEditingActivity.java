package com.example.galbenabu1.classscanner.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.galbenabu1.classscanner.R;
import com.fenchtose.nocropper.CropperView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Logic.ConvolutionMatrix;

public class ImageEditingActivity extends AppCompatActivity {

    private static final String TAG = "ImageEditingActivity";
    private Button finishEditingBtn;
    private ImageView sharpnessImageView, imageToEdit;
    private Bitmap currImage, oldImage;
    private SeekBar sb_value;
    private boolean isSharpnessClicked = false;
    private FirebaseStorage storage;
    private StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_editing);


        sb_value = (SeekBar) findViewById(R.id.sb_value);
        imageToEdit = (ImageView) findViewById(R.id.im_brightness);
        sharpnessImageView = (ImageView) findViewById(R.id.sharpnessImageView);
        finishEditingBtn = (Button) findViewById(R.id.finishEditingBtn);

        if (getIntent().hasExtra("IMAGE")) {
            Bitmap b = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("IMAGE"), 0, getIntent().getByteArrayExtra("IMAGE").length);
            imageToEdit.setImageBitmap(b);
            oldImage = ((BitmapDrawable) imageToEdit.getDrawable()).getBitmap();
            currImage = oldImage;
        }

        sb_value.setProgress(100);
        sb_value.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                imageToEdit.setColorFilter(setBrightness(progress));
                oldImage = ((BitmapDrawable) imageToEdit.getDrawable()).getBitmap();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sharpnessImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSharpnessImageViewClicked();
            }
        });
    }

    public static PorterDuffColorFilter setBrightness(int progress) {
        if (progress >= 100) {
            return new PorterDuffColorFilter(Color.argb(progress, 255, 255, 255), PorterDuff.Mode.SRC_OVER);
        } else {
            int value = (int) (100 - progress) * 255 / 100;
            return new PorterDuffColorFilter(Color.argb(value, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);
        }
    }


    public void onSharpnessImageViewClicked() {
        if (!isSharpnessClicked) {
            oldImage = currImage;
            isSharpnessClicked = true;
            imageToEdit.setImageBitmap(sharpenImage(currImage, currImage.getHeight() * currImage.getWidth()));
        } else {
            currImage = oldImage;
            isSharpnessClicked = false;
        }
    }

    public Bitmap sharpenImage(Bitmap src, double weight) {


        // set sharpness configuration
        double[][] SharpConfig = new double[][]{
                {0, -2, 0},
                {-2, weight, -2},
                {0, -2, 0}
        };
        //create convolution matrix instance
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        //apply configuration
        convMatrix.applyConfig(SharpConfig);
        //set weight according to factor
        convMatrix.Factor = weight - 8;
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }

}
