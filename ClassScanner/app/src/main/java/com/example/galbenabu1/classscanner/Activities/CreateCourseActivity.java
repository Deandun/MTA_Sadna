package com.example.galbenabu1.classscanner.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.example.galbenabu1.classscanner.R;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import Logic.Album;
import Logic.Course;
import Logic.Database.DBManager;

public class CreateCourseActivity extends AppCompatActivity {

    private final static String TAG = "CreateCourseActivity";

    private final static String NEW_ALBUM_DATA = "new_album_data";

    private List<Album> mAlbumsCollection;

    // UI
    private EditText metCourseName;
    private EditText metCreatorName;
    private EditText metCreateDate;
    private EditText metCourseDescription;
    private DBManager mDBManager = new DBManager();
    private Date mCreateDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate >>");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        this.getAlbumAndCourseID();
        this.bindUI();
        this.setUI();
    }

    private void setUI() {
        this.metCreatorName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    private void getAlbumAndCourseID() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        this.mAlbumsCollection = (List<Album>) extras.get(NEW_ALBUM_DATA);
    }

    private void bindUI(){
        this.metCourseName = findViewById(R.id.etCreateCourseName);
        this.metCourseDescription = findViewById(R.id.etCreateCouresDescription);
        this.metCreateDate = findViewById(R.id.etDate);
        this.metCreatorName = findViewById(R.id.etCreatorName);
    }


    public void onFinishCreatingCourseClick(View v) {
        Log.e(TAG, "onFinishCreatingCourse >>");


        Course newCourse = this.createNewCourse();

        this.mDBManager.addNewCourseDetailsToDBAndSetCourseID(newCourse);
        // Return to home screen
        Intent homeIntent = new Intent(CreateCourseActivity.this, HomeActivity.class);
        startActivity(homeIntent);

        Log.e(TAG, "onFinishCreatingCourse <<");

    }

    private Course createNewCourse() {
        Course newCourse = new Course();

        //TODO: finish setting newCourse's values.
        String courseName = this.metCreatorName.getText().toString();
        String courserDescription = this.metCourseDescription.getText().toString();
        String courseCreator = this.metCreatorName.getText().toString();
        this.mCreateDate = Calendar.getInstance().getTime();
        newCourse.setDescription(courserDescription);
        newCourse.setCourseName(courseName);
        newCourse.setCreatorName(courseCreator);

        Log.e(TAG, "************this.mCreateDate = " +   this.mCreateDate.toString());

        return newCourse;
    }


}
